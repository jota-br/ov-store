package ostro.veda.db;

import jakarta.persistence.OptimisticLockException;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailRepository extends Repository {

    public OrderDetailRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }

    public List<OrderDetailDTO> addOrderDetail(int orderId, Map<ProductDTO, Integer> productAndQuantity)
            throws ErrorHandling.InsufficientInventoryException, OptimisticLockException {

        Order order = this.getEm().find(Order.class, orderId);
        if (order == null) {
            return null;
        }

        List<OrderDetail> orderDetailList = new ArrayList<>();
        Map<Product, Integer> productDaoAndQuantity = new HashMap<>();
        List<Product> mergeList = new ArrayList<>();

        // Updates and put's Product, Quantity to the productDaoAndQuantity map
        inventoryCheck(productAndQuantity, productDaoAndQuantity);
        // Updates the DAO entity with the new stock value Adding or Subtracting (see Calculation enum)
        // and adds the updated object to the mergeList
        updateProductInventory(productDaoAndQuantity, mergeList, Calculation.SUBTRACTION);
        // If merge was successful and Product list is returned
        List<Product> productInventoryMerged = entityManagerHelper.executeMergeBatch(this.getEm(), mergeList);

        // if merge was unsuccessful a null value is returned
        if (productInventoryMerged == null) {
            return null;
        }

        for (Map.Entry<Product, Integer> entry : productDaoAndQuantity.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            orderDetailList.add(new OrderDetail(order, product, quantity, product.getPrice()));
        }

        orderDetailList = entityManagerHelper.executePersistBatch(this.getEm(), orderDetailList);

        if (orderDetailList == null || orderDetailList.isEmpty()) {
            int retries = 0;
            while (retries < 10) {
                mergeList = new ArrayList<>();
                undoInventoryChanges(productDaoAndQuantity, mergeList);
                List<Product> productInventoryRollback = entityManagerHelper.executeMergeBatch(this.getEm(), mergeList);
                if (productInventoryRollback != null) {
                    break;
                }
                retries++;
            }
            return null;
        }

        return getOrderDetailDTOList(orderDetailList);
    }

    private void inventoryCheck(Map<ProductDTO, Integer> productAndQuantity, Map<Product, Integer> productDaoAndQuantity) throws ErrorHandling.InsufficientInventoryException {
        for (Map.Entry<ProductDTO, Integer> entry : productAndQuantity.entrySet()) {
            Product product = this.getEm().find(Product.class, entry.getKey().getProductId());
            int quantity = entry.getValue();
            if (product.getStock() >= quantity) {
                productDaoAndQuantity.put(product, quantity);
            } else {
                throw new ErrorHandling.InsufficientInventoryException();
            }
        }
    }

    private void updateProductInventory(Map<Product, Integer> productDaoAndQuantity, List<Product> mergeList, Calculation calculation) {
        for (Map.Entry<Product, Integer> entry : productDaoAndQuantity.entrySet()) {
            Product product = entry.getKey();
            int newStock = product.getStock();
            switch (calculation) {
                case SUM -> newStock = newStock + entry.getValue();
                case SUBTRACTION -> newStock = newStock - entry.getValue();
            }

            product = product.updateStock(newStock);
            mergeList.add(product);
        }
    }

    private void undoInventoryChanges(Map<Product, Integer> productAndQuantityUpdated, List<Product> mergeList) {
        updateProductInventory(productAndQuantityUpdated, mergeList, Calculation.SUM);
    }

    private static List<OrderDetailDTO> getOrderDetailDTOList(List<OrderDetail> orderDetailList) {
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetailDTOList.add(orderDetail.transformToDto());
        }
        return orderDetailDTOList;
    }

    private enum Calculation {
        SUM,
        SUBTRACTION;
    }
}
