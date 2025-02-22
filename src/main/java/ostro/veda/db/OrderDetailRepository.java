package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.OrderValidation;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;

import java.util.ArrayList;
import java.util.HashMap;
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
    public List<OrderDetailDTO> addOrder(Map<ProductDTO, Integer> productAndQuantity, Order order)
            throws OptimisticLockException, ErrorHandling.InvalidInputException {

        List<OrderDetail> orderDetailList = new ArrayList<>();
        Map<Product, Integer> productDaoAndQuantity = new HashMap<>();

        // Updates and put's Product, Quantity to the productDaoAndQuantity map
        inventoryValidation(productAndQuantity, productDaoAndQuantity);
        // Updates the DAO entity with the new stock value Adding or Subtracting (see Calculation enum)
        // and adds the updated object to the mergeList
        List<Product> mergeList = updateProductInventory(productDaoAndQuantity, Calculation.SUBTRACTION);
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

    public void cancelOrder(Map<Product, Integer> productDaoAndQuantity, Calculation calculation) {
        List<Product> mergeList = updateProductInventory(productDaoAndQuantity, calculation);
        for (Product product : mergeList) {
            this.em.persist(product);
        }
    }

    /**
     *
     * @param productAndQuantity ProductDTO is used to get the most up-to-date Product information
     *                           required to check stock value and validate the order quantity against.
     * @param productDaoAndQuantity Map containing the Product DAO entity to be updated and merged
     *                              with sold quantity.
     *                              While this method returns no value, the Map<Product, Integer> is a reference
     *                              to the map with the same name in addOrderStatusHistory.
     * @throws ErrorHandling.InvalidInputException input is invalid and a customized Exception in returned with
     * the Exception message and the reject input.
     */
    private void inventoryValidation(Map<ProductDTO, Integer> productAndQuantity, Map<Product, Integer> productDaoAndQuantity)
            throws ErrorHandling.InvalidInputException {
        for (Map.Entry<ProductDTO, Integer> entry : productAndQuantity.entrySet()) {
            Product product = this.em.find(Product.class, entry.getKey().getProductId());
            int quantity = entry.getValue();
            if (!OrderValidation.hasValidInput(product, quantity)) continue;
            productDaoAndQuantity.put(product, quantity);
        }
    }

    /**
     *
     * @param productDaoAndQuantity Entity to be updated and new updated value.
     * @param calculation If a new order is placed the Calculation is a SUM
     *                    if it's a cancellation the Calculation is a SUBTRACTION
     */
    private List<Product> updateProductInventory(Map<Product, Integer> productDaoAndQuantity, Calculation calculation) {
        List<Product> mergeList = new ArrayList<>();
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

    private void undoInventoryChanges(Map<Product, Integer> productAndQuantityUpdated) {
        updateProductInventory(productAndQuantityUpdated, Calculation.SUM);
    }
}
