package ostro.veda.common.dto;

import ostro.veda.db.jpa.Address;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private final int orderId;
    private final int userId;
    private final LocalDateTime orderDate;
    private final double totalAmount;
    private final String status;
    private final List<OrderDetailDTO> orderDetails;
    private final Address shippingAddress;
    private final Address billingAddress;
    private final List<OrderStatusHistoryDTO> orderStatusHistory;

    public OrderDTO(int orderId, int userId, LocalDateTime orderDate, double totalAmount,
                    String status, List<OrderDetailDTO> orderDetails, Address shippingAddress,
                    Address billingAddress, List<OrderStatusHistoryDTO> orderStatusHistory) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.orderStatusHistory = orderStatusHistory;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public List<OrderStatusHistoryDTO> getOrderStatusHistory() {
        return orderStatusHistory;
    }
}
