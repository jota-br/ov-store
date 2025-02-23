package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.OrderValidation;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDetailRepository extends Repository {

    public OrderDetailRepository(EntityManager em) {
        super(em);
    }

    public enum Calculation {
        SUM,
        SUBTRACTION;
    }

    /**
     * @param productAndQuantity Map containing the Product and quantity
     *                           allocated for this order.
     * @param order Order entity for which the OrderDetail is being created for.
     * @return OrderDetailDTO list to include in the OrderDTO Detail list.
     * @throws OptimisticLockException for object @version in Product entity
     * this ensures data integrity.
     * @throws ErrorHandling.InvalidInputException input is invalid and a customized Exception in returned with
     * the Exception message and the reject input.
     */
    public List<OrderDetailDTO> addOrderDetail(Map<Product, Integer> productAndQuantity, Order order, Calculation calculation)
            throws OptimisticLockException, ErrorHandling.InvalidInputException {

        List<OrderDetail> orderDetailList = new ArrayList<>();
        inventoryValidation(productAndQuantity);
        List<Product> mergeList = updateProductInventory(productAndQuantity, calculation);

        for (Product product : mergeList) {
            this.em.persist(product);
        }

        for (Map.Entry<Product, Integer> entry : productAndQuantity.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            orderDetailList.add(new OrderDetail(order, product, quantity, product.getPrice()));
        }

        for (OrderDetail orderDetail : orderDetailList) {
            this.em.persist(orderDetail);
        }

        return getOrderDetailDTOList(orderDetailList);
    }

    public void cancelOrder(Map<Product, Integer> productDaoAndQuantity, Calculation calculation) {
        List<Product> mergeList = updateProductInventory(productDaoAndQuantity, calculation);
        for (Product product : mergeList) {
            this.em.persist(product);
        }
    }

    /**
     * @param productAndQuantity Map containing the Product DAO entity to be updated and merged
     *                              with sold quantity.
     *                              While this method returns no value, the Map<Product, Integer> is a reference
     *                              to the map with the same name in addOrderStatusHistory.
     * @throws ErrorHandling.InvalidInputException input is invalid and a customized Exception in returned with
     * the Exception message and the reject input.
     */
    private void inventoryValidation(Map<Product, Integer> productAndQuantity)
            throws ErrorHandling.InvalidInputException {
        for (Map.Entry<Product, Integer> entry : productAndQuantity.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            if (!OrderValidation.hasValidInput(product, quantity)) continue;
            productAndQuantity.put(product, quantity);
        }
    }

    /**
     *
     * @param productAndQuantity Entity to be updated and new updated value.
     * @param calculation If a new order is placed the Calculation is a SUM
     *                    if it's a cancellation the Calculation is a SUBTRACTION
     */
    private List<Product> updateProductInventory(Map<Product, Integer> productAndQuantity, Calculation calculation) {
        List<Product> mergeList = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : productAndQuantity.entrySet()) {
            Product product = entry.getKey();
            int newStock = product.getStock();
            switch (calculation) {
                case SUM -> newStock = newStock + entry.getValue();
                case SUBTRACTION -> newStock = newStock - entry.getValue();
            }

            product = product.updateStock(newStock);
            mergeList.add(product);
        }
        return mergeList;
    }

    /**
     *
     * @param orderDetailList OrderDetail entity which was merged it transformed to DTO
     * @return returns the persisted OrderDetailDTO entities
     */
    private static List<OrderDetailDTO> getOrderDetailDTOList(List<OrderDetail> orderDetailList) {
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetailDTOList.add(orderDetail.transformToDto());
        }
        return orderDetailDTOList;
    }
}
