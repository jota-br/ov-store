package main.java.ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.OrderDTO;
import main.java.ostro.veda.common.dto.OrderDetailDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.common.validation.ValidateUtil;
import main.java.ostro.veda.db.OrderRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepositoryImpl;

    public OrderServiceImpl(OrderRepository orderRepositoryImpl) {
        this.orderRepositoryImpl = orderRepositoryImpl;
    }

    @Override
    public OrderDTO add(@NonNull OrderDTO orderDTO) {
        try {
            log.info("add() new Order for User = {}", orderDTO.getUserId());
            ValidateUtil.validateOrder(orderDTO);
            return orderRepositoryImpl.add(orderDTO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public OrderDTO update(@NonNull OrderDTO orderDTO) {
        try {
            log.info("update() OrderStatus for Order = {}", orderDTO.getOrderId());
            ValidateUtil.validateOrderIdAndStatus(orderDTO.getOrderId(), orderDTO.getStatus());
            return orderRepositoryImpl.update(orderDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public OrderDTO cancelOrder(int orderId) {
        try {
            log.info("cancelOrder() Order = {}", orderId);
            ValidateUtil.validateId(orderId);
            return  orderRepositoryImpl.cancelOrder(orderId);
        } catch (UnsupportedOperationException | ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public OrderDTO returnItem(@NonNull OrderDetailDTO returningItem) {
        try {
            log.info("returnItem() Product = {}", returningItem.getProduct().getProductId());
            return orderRepositoryImpl.returnItem(returningItem);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
