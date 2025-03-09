package ostro.veda.repository.interfaces;

import jakarta.validation.constraints.NotNull;
import ostro.veda.model.dto.OrderDetailDto;
import ostro.veda.model.dto.OrderDto;

public interface OrderRepository extends Repository<OrderDto> {

    OrderDto cancelOrder(int orderId);
    OrderDto returnItem(@NotNull OrderDetailDto orderDetailDTO);
}
