package ostro.veda.service.events;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import ostro.veda.common.dto.*;
import ostro.veda.common.util.Action;

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
    private final CouponDTO couponDTO;

    @Builder
    public AuditEvent(Object source, @NonNull Action action, int userId, int id, AddressDTO addressDTO,
                      CategoryDTO categoryDTO, OrderDTO orderDTO, ProductDTO productDTO,
                      UserDTO userDTO, CouponDTO couponDTO) {
        super(source);
        this.action = action;
        this.userId = userId;
        this.id = id;
        this.addressDTO = addressDTO;
        this.categoryDTO = categoryDTO;
        this.orderDTO = orderDTO;
        this.productDTO = productDTO;
        this.userDTO = userDTO;
        this.couponDTO = couponDTO;
    }
}
