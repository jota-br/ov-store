package ostro.veda.service.events;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import ostro.veda.common.dto.*;
import ostro.veda.db.helpers.database.Action;

@Slf4j
@Getter
public class AuditEvent extends ApplicationEvent {

    private final Action action;
    private final int userId;
    private final int id;

    private final AddressDTO addressDTO;
    private final CategoryDTO categoryDTO;
    private final OrderDTO orderDTO;
    private final ProductDTO productDTO;
    private final UserDTO userDTO;

    @Builder
    public AuditEvent(Object source, Action action, int userId, int id, AddressDTO addressDTO, CategoryDTO categoryDTO, OrderDTO orderDTO, ProductDTO productDTO, UserDTO userDTO) {
        super(source);
        log.info("AuditEvent() created");

        if ((action == null) || (addressDTO == null && categoryDTO == null &&  orderDTO == null && productDTO == null && userDTO == null))
            throw new IllegalArgumentException("Source, Action and at least one DTO must be provided");

        this.action = action;
        this.addressDTO = addressDTO;
        this.categoryDTO = categoryDTO;
        this.orderDTO = orderDTO;
        this.productDTO = productDTO;
        this.userDTO = userDTO;
        this.userId = userId;
        this.id = id;
    }
}
