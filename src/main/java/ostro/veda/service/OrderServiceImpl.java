package ostro.veda.service;

import lombok.NonNull;
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
import ostro.veda.util.exception.InputException;
import ostro.veda.util.validation.ValidatorUtil;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    private final ValidatorUtil validatorUtil;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepositoryImpl;

    public OrderServiceImpl(ValidatorUtil validatorUtil, ApplicationEventPublisher applicationEventPublisher, OrderRepository orderRepositoryImpl) {
        this.validatorUtil = validatorUtil;
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderRepositoryImpl = orderRepositoryImpl;
    }

    @Override
    public OrderDto add(@NonNull OrderDto orderDTO) {

        try {

            log.info("add() new Order for User = {}", orderDTO.getUserId());

            validatorUtil.validate(orderDTO);

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
    public OrderDto update(@NonNull OrderDto orderDTO) {

        try {

            log.info("update() OrderStatus for Order = {}", orderDTO.getOrderId());

            validatorUtil.validate(orderDTO);

            orderDTO = orderRepositoryImpl.update(orderDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, orderDTO, 1);

            return orderDTO;

        } catch (InputException.InvalidInputException e) {

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
    public OrderDto returnItem(@NonNull OrderDetailDto returningItem) {
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
