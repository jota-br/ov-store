package ostro.veda.service.interfaces;


import ostro.veda.model.dto.OrderDto;
import ostro.veda.model.dto.OrderDetailDto;

public interface OrderService extends Service<OrderDto> {

    OrderDto cancelOrder(int orderId);

    OrderDto returnItem(OrderDetailDto returningItem);
}
