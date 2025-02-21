package ostro.veda.service;

import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.OrderValidation;
import ostro.veda.db.OrderRepository;
import ostro.veda.db.jpa.Address;
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
     * @return the OrderDTO after persisted.
     */
    public OrderDTO addOrder(int userId, double totalAmount, String status,
                             Address shippingAddress, Address billingAddress,
                             Map<ProductDTO, Integer> productAndQuantity) {
        try {
            if (!hasValidInput(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity)) return null;
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
     * @return updated OrderDTO
     * @throws ErrorHandling.InvalidInputException input is invalid and a customized Exception in returned with
     * the Exception message and the reject input.
     */
    public OrderDTO updateOrderStatus(int orderId, String newStatus)
            throws ErrorHandling.InvalidInputException {
        if (!hasValidInput(orderId, newStatus)) return null;
        return orderRepository.updateOrderStatus(orderId, newStatus);
    }

    /**
     * parameters to be checked for valid input
     * @param userId requires a valid userId
     * @param totalAmount requires a valid total amount
     * @param status requires a valid OrderStatus
     * @param shippingAddress requires a valid shipping address
     * @param billingAddress requires a valid billing address
     * @param productAndQuantity will check if the product is valid and if it has the required stock
     * @return true if input is validated successfully
     * @throws ErrorHandling.InvalidInputException input is invalid and a customized Exception in returned with
     * the Exception message and the reject input.
     */
    private boolean hasValidInput(int userId, double totalAmount, String status, Address shippingAddress,
                                        Address billingAddress, Map<ProductDTO, Integer> productAndQuantity)
            throws ErrorHandling.InvalidInputException {
        return OrderValidation.hasValidInput(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity);
    }

    /**
     *
     * @param orderId requires a valid userId
     * @param status requires a valid OrderStatus
     * @return true if input is validated successfully
     * @throws ErrorHandling.InvalidInputException input is invalid and a customized Exception in returned with
     * the Exception message and the reject input.
     */
    private boolean hasValidInput(int orderId, String status) throws ErrorHandling.InvalidInputException {
        return OrderValidation.hasValidInput(orderId, status);
    }
}
