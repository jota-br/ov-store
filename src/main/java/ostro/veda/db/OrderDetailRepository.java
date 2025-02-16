package ostro.veda.db;

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
            throws ErrorHandling.InsufficientInventoryException, ErrorHandling.UnableToPersistException {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        Map<Product, Integer> productAndQuantityUpdated = new HashMap<>();
        List<Product> mergeList = new ArrayList<>();

        inventoryCheck(productAndQuantity, productAndQuantityUpdated);
        updateProductInventory(productAndQuantityUpdated, mergeList, Calculation.SUBTRACTION);
        List<Product> productInventoryUpdated = entityManagerHelper.executeMergeBatch(this.getEm(), mergeList);

        if (productInventoryUpdated == null) {
            return null;
        }

        Order order = this.getEm().find(Order.class, orderId);
        if (order == null) {
            return null;
        }

        for (Map.Entry<Product, Integer> entry : productAndQuantityUpdated.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            orderDetailList.add(new OrderDetail(order, product, quantity, product.getPrice()));
        }

        orderDetailList = entityManagerHelper.executePersistBatch(this.getEm(), orderDetailList);

        mergeList = new ArrayList<>();
        if (orderDetailList == null || orderDetailList.isEmpty()) {
            undoInventoryChanges(productAndQuantityUpdated, mergeList);
            return null;
        }

        return getOrderDetailDTOList(orderDetailList);
    }

    private void inventoryCheck(Map<ProductDTO, Integer> productAndQuantity, Map<Product, Integer> productAndQuantityUpdated) throws ErrorHandling.InsufficientInventoryException {
        for (Map.Entry<ProductDTO, Integer> entry : productAndQuantity.entrySet()) {
            Product product = this.getEm().find(Product.class, entry.getKey().getProductId());
            int quantity = entry.getValue();
            if (product.getStock() <= quantity) {
                productAndQuantityUpdated.put(product, quantity);
            } else {
                throw new ErrorHandling.InsufficientInventoryException();
            }
        }
    }

    private void updateProductInventory(Map<Product, Integer> productAndQuantityUpdated, List<Product> mergeList, Calculation calculation) {
        for (Map.Entry<Product, Integer> entry : productAndQuantityUpdated.entrySet()) {
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

    private void undoInventoryChanges(Map<Product, Integer> productAndQuantityUpdated, List<Product> mergeList)
            throws ErrorHandling.UnableToPersistException {
        List<Product> productInventoryUpdated;
        updateProductInventory(productAndQuantityUpdated, mergeList, Calculation.SUM);
        entityManagerHelper.executeMergeBatch(this.getEm(), mergeList);

        throw new ErrorHandling.UnableToPersistException();
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
