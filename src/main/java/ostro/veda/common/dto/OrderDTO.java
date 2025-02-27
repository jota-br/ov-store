package ostro.veda.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private final int orderId;
    private final int userId;
    private final LocalDateTime orderDate;
    private final double totalAmount;
    private final String status;
    private final List<OrderDetailDTO> orderDetails;
    private final AddressDTO shippingAddress;
    private final AddressDTO billingAddress;
    private final List<OrderStatusHistoryDTO> orderStatusHistory;
    private final LocalDateTime updatedAt;

    public OrderDTO(int orderId, int userId, LocalDateTime orderDate, double totalAmount,
                    String status, List<OrderDetailDTO> orderDetails, AddressDTO shippingAddress,
                    AddressDTO billingAddress, List<OrderStatusHistoryDTO> orderStatusHistory, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails == null ? new ArrayList<>() : orderDetails;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.orderStatusHistory = orderStatusHistory == null ? new ArrayList<>() : orderStatusHistory;
        this.updatedAt = updatedAt;
    }

    public OrderDTO(int userId, double totalAmount, String status,
                    AddressDTO shippingAddress, AddressDTO billingAddress) {
        this(0, userId, null, totalAmount, status, null,
                shippingAddress, billingAddress, null, null);
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

    public AddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public AddressDTO getBillingAddress() {
        return billingAddress;
    }

    public List<OrderStatusHistoryDTO> getOrderStatusHistory() {
        return orderStatusHistory;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
