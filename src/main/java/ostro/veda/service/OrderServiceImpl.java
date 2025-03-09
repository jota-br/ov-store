package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.OrderDetailDto;
import ostro.veda.model.dto.OrderDto;
import ostro.veda.repository.interfaces.OrderRepository;
import ostro.veda.service.events.EventPayload;
import ostro.veda.service.interfaces.OrderService;
import ostro.veda.util.constant.ServiceMethodName;
import ostro.veda.util.enums.Action;

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
    public OrderDto add(OrderDto orderDTO) {

        try {

            log.info("add() new Order for User = {}", orderDTO.getUserId());

            orderDTO = orderRepositoryImpl.add(orderDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, orderDTO, 1);

            if (orderDTO.getCoupon() != null)
                this.applicationEventPublisher.publishEvent(
                        new EventPayload(this, orderDTO.getCoupon(), ServiceMethodName.UPDATE)
                );

            return orderDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public OrderDto update(OrderDto orderDTO) {

        try {

            log.info("update() OrderStatus for Order = {}", orderDTO.getOrderId());

            orderDTO = orderRepositoryImpl.update(orderDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, orderDTO, 1);

            return orderDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public OrderDto cancelOrder(int orderId) {

        try {

            log.info("cancelOrder() Order = {}", orderId);

            OrderDto orderDTO = orderRepositoryImpl.cancelOrder(orderId);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, orderDTO, 1);

            return orderDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public OrderDto returnItem(OrderDetailDto returningItem) {
        try {

            log.info("returnItem() Product = {}", returningItem.getProduct().getProductId());

            OrderDto orderDTO = orderRepositoryImpl.returnItem(returningItem);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, orderDTO, 1);

            return orderDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }
}
