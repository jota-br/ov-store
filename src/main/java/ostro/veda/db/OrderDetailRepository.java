package ostro.veda.db;

import jakarta.persistence.EntityManager;
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

    public OrderDetailRepository(EntityManager em, EntityManagerHelper entityManagerHelper) {
        super(em, entityManagerHelper);
    }

    public List<OrderDetailDTO> addOrderDetail(Map<ProductDTO, Integer> productAndQuantity, Order order)
            throws ErrorHandling.InsufficientInventoryException, OptimisticLockException {

        List<OrderDetail> orderDetailList = new ArrayList<>();
        Map<Product, Integer> productDaoAndQuantity = new HashMap<>();
        List<Product> mergeList = new ArrayList<>();

        // Updates and put's Product, Quantity to the productDaoAndQuantity map
        inventoryCheck(productAndQuantity, productDaoAndQuantity);
        // Updates the DAO entity with the new stock value Adding or Subtracting (see Calculation enum)
        // and adds the updated object to the mergeList
        updateProductInventory(productDaoAndQuantity, mergeList, Calculation.SUBTRACTION);
        // If merge was successful and Product list is returned
        for (Product product : mergeList) {
            this.em.persist(product);
        }

        for (Map.Entry<Product, Integer> entry : productDaoAndQuantity.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            orderDetailList.add(new OrderDetail(order, product, quantity, product.getPrice()));
        }

        for (OrderDetail orderDetail : orderDetailList) {
            this.em.persist(orderDetail);
        }

        return getOrderDetailDTOList(orderDetailList);
    }

    private void inventoryCheck(Map<ProductDTO, Integer> productAndQuantity, Map<Product, Integer> productDaoAndQuantity) throws ErrorHandling.InsufficientInventoryException {
        for (Map.Entry<ProductDTO, Integer> entry : productAndQuantity.entrySet()) {
            Product product = this.em.find(Product.class, entry.getKey().getProductId());
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
