package ostro.veda.service;

import ostro.veda.common.dto.OrderBasic;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.OrderRepository;
import ostro.veda.loggerService.Logger;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO addOrder(OrderBasic orderBasic) {
        try {
            ValidateUtil.validateOrder(orderBasic);
            return orderRepository.addOrder(orderBasic);
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
        ValidateUtil.validateOrderIdAndStatus(orderId, newStatus);
        return orderRepository.updateOrderStatus(orderId, newStatus);
    }

    /**
     * Requires single EntityManager injection
     * @param orderId requires a valid orderId
     * @return OrderDTO
     */
    public OrderDTO cancelOrder(int orderId) {
        try {
            ValidateUtil.validateId(orderId);
            return  orderRepository.cancelOrder(orderId);
        } catch (UnsupportedOperationException | ErrorHandling.InvalidInputException e) {
            Logger.log(e);
            return null;
        }
    }

    public OrderDTO returnItem(OrderBasic orderBasic) {
        try {
            return orderRepository.returnItem(orderBasic);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
