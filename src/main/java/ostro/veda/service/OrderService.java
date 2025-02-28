package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.OrderBasic;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.OrderRepository;

@Slf4j
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
            log.warn(e.getMessage());
            return null;
        }
    }


    public OrderDTO updateOrderStatus(int orderId, String newStatus)
            throws ErrorHandling.InvalidInputException {
        ValidateUtil.validateOrderIdAndStatus(orderId, newStatus);
        return orderRepository.updateOrderStatus(orderId, newStatus);
    }

    public OrderDTO cancelOrder(int orderId) {
        try {
            ValidateUtil.validateId(orderId);
            return  orderRepository.cancelOrder(orderId);
        } catch (UnsupportedOperationException | ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public OrderDTO returnItem(OrderBasic orderBasic) {
        try {
            return orderRepository.returnItem(orderBasic);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
