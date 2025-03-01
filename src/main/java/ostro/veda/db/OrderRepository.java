package ostro.veda.db;

import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.OrderStatusHistory;

import java.util.List;

public interface OrderRepository extends Repository<OrderDTO> {

    OrderDTO cancelOrder(int orderId);

    Order buildOrder(OrderDTO orderDTO);
    Order buildNewOrderStatusHistory(Order order);

    List<OrderDetail> buildOrderDetail(Order order, List<OrderDetailDTO> orderDetailDTOS);

    List<OrderStatusHistory> buildOrderStatusHistories(Order order, List<OrderStatusHistoryDTO> orderStatusHistoryDTOS);
    OrderStatusHistory buildOrderStatusHistory(Order order, OrderStatusHistoryDTO orderStatusHistoryDTO);
}
