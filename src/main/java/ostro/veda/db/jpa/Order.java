package ostro.veda.db.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.db.helpers.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "status")
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "address_id")
    private Address billingAddress;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Order() {
    }

    public Order(int userId, LocalDateTime orderDate, double totalAmount, OrderStatus status,
                 List<OrderDetail> orderDetails, Address shippingAddress, Address billingAddress, LocalDateTime updatedAt) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDetails = orderDetails;
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

    public Order updateOrder(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderDTO transformToDTO() {
        return new OrderDTO(this.getOrderId(), this.getUserId(), this.getOrderDate(), this.getTotalAmount(), this.getStatus(),
                this.getOrderDetails(), this.getShippingAddress(), this.getBillingAddress(), this.getUpdatedAt());
    }
}
