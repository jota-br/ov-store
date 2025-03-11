package ostro.veda.service.interfaces;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ostro.veda.model.dto.OrderDetailDto;
import ostro.veda.model.dto.OrderDto;
import ostro.veda.util.validation.annotation.ValidId;

public interface OrderService extends Service<OrderDto> {

    OrderDto cancelOrder(@ValidId int orderId);

    OrderDto returnItem(@NotNull @Valid OrderDetailDto returningItem);
}
