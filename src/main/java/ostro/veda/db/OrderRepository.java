package main.java.ostro.veda.db;

import main.java.ostro.veda.common.dto.OrderDTO;
import main.java.ostro.veda.common.dto.OrderDetailDTO;
import main.java.ostro.veda.common.dto.OrderStatusHistoryDTO;
import main.java.ostro.veda.db.jpa.Order;
import main.java.ostro.veda.db.jpa.OrderDetail;
import main.java.ostro.veda.db.jpa.OrderStatusHistory;

import java.util.List;

public interface OrderRepository extends Repository<OrderDTO> {

    OrderDTO cancelOrder(int orderId);
    OrderDTO returnItem(OrderDetailDTO orderDetailDTO);

    Order buildOrder(OrderDTO orderDTO);
    Order buildNewOrderStatusHistory(Order order);

    List<OrderDetail> buildOrderDetails(Order order, List<OrderDetailDTO> orderDetailDTOS);
    OrderDetail buildOrderDetail(Order order, OrderDetailDTO orderDetailDTO);

    List<OrderStatusHistory> buildOrderStatusHistories(Order order, List<OrderStatusHistoryDTO> orderStatusHistoryDTOS);
    OrderStatusHistory buildOrderStatusHistory(Order order, OrderStatusHistoryDTO orderStatusHistoryDTO);
}
