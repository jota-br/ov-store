package main.java.ostro.veda.service;


import main.java.ostro.veda.common.dto.OrderDTO;
import main.java.ostro.veda.common.dto.OrderDetailDTO;

public interface OrderService extends Service<OrderDTO> {

    OrderDTO cancelOrder(int orderId);

    OrderDTO returnItem(OrderDetailDTO returningItem);
}
