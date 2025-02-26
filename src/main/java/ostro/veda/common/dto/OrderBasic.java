package ostro.veda.common.dto;

import java.util.Collections;
import java.util.List;

public class OrderBasic {

    private final int userId;
    private final int orderId;
    private final String status;
    private final int billingAddressId;
    private final int shippingAddressId;
    private final List<OrderDetailBasic> orderDetails;
    private final double totalAmount;

    public OrderBasic(int userId, int orderId, String status, int billingAddressId, int shippingAddressId, List<OrderDetailBasic> orderDetails, double totalAmount) {
        this.userId = userId;
        this.orderId = orderId;
        this.status = status;
        this.billingAddressId = billingAddressId;
        this.shippingAddressId = shippingAddressId;
        this.orderDetails = Collections.unmodifiableList(orderDetails);
        this.totalAmount = totalAmount;
    }

    public OrderBasic(int userId, String status, int billingAddressId, int shippingAddressId, List<OrderDetailBasic> orderDetails, double totalAmount) {
        this(userId, -1, status, billingAddressId, shippingAddressId, orderDetails, totalAmount);
    }

    public OrderBasic(int userId, int orderId, String status, int billingAddressId, int shippingAddressId, List<OrderDetailBasic> orderDetails) {
        this(userId, orderId, status, billingAddressId, shippingAddressId, orderDetails, 0);
    }

    public OrderBasic(int userId, String status, int billingAddressId, int shippingAddressId, List<OrderDetailBasic> orderDetails) {
        this(userId, -1, status, billingAddressId, shippingAddressId, orderDetails, 0);
    }

    public int getUserId() {
        return userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public int getBillingAddressId() {
        return billingAddressId;
    }

    public int getShippingAddressId() {
        return shippingAddressId;
    }

    public List<OrderDetailBasic> getOrderDetails() {
        return orderDetails;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

