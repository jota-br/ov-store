package ostro.veda.service;


import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;

public interface OrderService extends Service<OrderDTO> {

    OrderDTO cancelOrder(int orderId);

    OrderDTO returnItem(OrderDetailDTO returningItem);
}
