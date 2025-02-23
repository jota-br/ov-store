package ostro.veda.service;

import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.OrderValidation;
import ostro.veda.db.OrderRepository;
import ostro.veda.loggerService.Logger;

import java.util.Map;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     *
     * @param userId userId to retrieved user placing the order, needs to be higher than 0.
     * @param totalAmount totalAmount of the order, can't be negative.
     * @param status the OrderStatus String value, will be checked against the enum values.
     * @param shippingAddress user shipping address
     * @param billingAddress user billing address
     * @param productAndQuantity Product and Quantity sold.
     * @return OrderDTO
     */
    public OrderDTO addOrder(int userId, double totalAmount, String status,
                             AddressDTO shippingAddress, AddressDTO billingAddress,
                             Map<ProductDTO, Integer> productAndQuantity) {
        try {
            if (!OrderValidation.hasValidInput(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity)) return null;
            return orderRepository.addOrder(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    /**
     *
     * @param orderId will be used to find the order and create the DAO to persist the new order status
     * @param newStatus the new status value to be persisted
     * @return OrderDTO
     * @throws ErrorHandling.InvalidInputException input is invalid and a customized Exception in returned with
     * the Exception message and the reject input.
     */
    public OrderDTO updateOrderStatus(int orderId, String newStatus)
            throws ErrorHandling.InvalidInputException {
        if (!OrderValidation.hasValidInput(orderId, newStatus)) return null;
        return orderRepository.updateOrderStatus(orderId, newStatus);
    }

    /**
     * Requires single EntityManager injection
     * @param orderId requires a valid orderId
     * @return OrderDTO
     */
    public OrderDTO cancelOrder(int orderId) {
        try {
            if (!OrderValidation.hasValidInput(orderId)) return null;
            return  orderRepository.cancelOrder(orderId);
        } catch (ErrorHandling.InvalidInputException | UnsupportedOperationException e) {
            Logger.log(e);
            return null;
        }
    }

    /**
     * orderId will be used to check the OrderDetail, product bought and quantity
     * @param orderId order identifier from the returning product
     * @param productAndQuantity product and quantity to be returned
     * @return OrderDTO
     */
    public OrderDTO returnItem(int orderId, Map<ProductDTO, Integer> productAndQuantity) {
        try {
            if (!OrderValidation.hasValidInput(orderId, productAndQuantity)) return null;
            return orderRepository.returnItem(orderId, productAndQuantity);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
