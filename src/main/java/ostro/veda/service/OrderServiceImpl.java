package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.util.Action;
import ostro.veda.common.util.MainServicesMethodsNames;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.OrderRepository;
import ostro.veda.service.events.EventPayload;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepositoryImpl;

    public OrderServiceImpl(ApplicationEventPublisher applicationEventPublisher, OrderRepository orderRepositoryImpl) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderRepositoryImpl = orderRepositoryImpl;
    }

    @Override
    public OrderDTO add(@NonNull OrderDTO orderDTO) {
        try {
            log.info("add() new Order for User = {}", orderDTO.getUserId());
            ValidateUtil.validateOrder(orderDTO);

            orderDTO = orderRepositoryImpl.add(orderDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, orderDTO, 1);

            if (orderDTO.getCoupon() != null)
                this.applicationEventPublisher.publishEvent(
                        new EventPayload(this, orderDTO.getCoupon(), MainServicesMethodsNames.UPDATE)
                );

            return orderDTO;

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

            orderDTO = orderRepositoryImpl.update(orderDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, orderDTO, 1);

            return orderDTO;

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

            OrderDTO orderDTO = orderRepositoryImpl.cancelOrder(orderId);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, orderDTO, 1);

            return orderDTO;

        } catch (UnsupportedOperationException | ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public OrderDTO returnItem(@NonNull OrderDetailDTO returningItem) {
        try {
            log.info("returnItem() Product = {}", returningItem.getProduct().getProductId());

            OrderDTO orderDTO = orderRepositoryImpl.returnItem(returningItem);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, orderDTO, 1);

            return orderDTO;

        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
