package ostro.veda.repository.interfaces;

import ostro.veda.model.dto.OrderDto;
import ostro.veda.model.dto.OrderDetailDto;
import ostro.veda.model.dto.OrderStatusHistoryDto;
import ostro.veda.repository.dao.Order;
import ostro.veda.repository.dao.OrderDetail;
import ostro.veda.repository.dao.OrderStatusHistory;

import java.util.List;

public interface OrderRepository extends Repository<OrderDto> {

    OrderDto cancelOrder(int orderId);
    OrderDto returnItem(OrderDetailDto orderDetailDTO);

    Order buildOrder(OrderDto orderDTO);
    Order buildNewOrderStatusHistory(Order order);

    List<OrderDetail> buildOrderDetails(Order order, List<OrderDetailDto> orderDetailDtos);
    OrderDetail buildOrderDetail(Order order, OrderDetailDto orderDetailDTO);

    List<OrderStatusHistory> buildOrderStatusHistories(Order order, List<OrderStatusHistoryDto> orderStatusHistoryDtos);
    OrderStatusHistory buildOrderStatusHistory(Order order, OrderStatusHistoryDto orderStatusHistoryDTO);
}
