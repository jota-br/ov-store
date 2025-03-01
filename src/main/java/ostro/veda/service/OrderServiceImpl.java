package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.OrderRepositoryImpl;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryImpl orderRepositoryImpl;

    public OrderServiceImpl(OrderRepositoryImpl orderRepositoryImpl) {
        this.orderRepositoryImpl = orderRepositoryImpl;
    }

    @Override
    public OrderDTO add(@NonNull OrderDTO orderDTO) {
        try {
            log.info("add() new Order for User = {}", orderDTO.getUserId());
            ValidateUtil.validateOrder(orderDTO);
            return orderRepositoryImpl.addOrder(orderDTO);
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
            return orderRepositoryImpl.updateOrderStatus(orderDTO);
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
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
