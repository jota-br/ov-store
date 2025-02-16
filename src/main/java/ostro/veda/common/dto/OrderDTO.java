package ostro.veda.common.dto;

import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private final int orderId;
    private final int userId;
    private final LocalDateTime orderDate;
    private final double totalAmount;
    private final OrderStatus status;
    private final List<OrderDetail> orderDetails;
    private final Address shippingAddress;
    private final Address billingAddress;
    private final LocalDateTime updatedAt;

    public OrderDTO(int orderId, int userId, LocalDateTime orderDate, double totalAmount,
                    OrderStatus status, List<OrderDetail> orderDetails, Address shippingAddress, Address billingAddress, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.updatedAt = updatedAt;
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

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
