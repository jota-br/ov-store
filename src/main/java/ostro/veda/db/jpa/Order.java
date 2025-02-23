package ostro.veda.db.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.OrderStatusHistoryDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @CreationTimestamp
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "address_id")
    private Address billingAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderStatusHistory> orderStatusHistory = new ArrayList<>();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Order() {
    }

    public Order(int userId, double totalAmount, String status,
                 Address shippingAddress, Address billingAddress) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public List<OrderStatusHistory> getOrderStatusHistory() {
        return orderStatusHistory;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Order updateOrderStatus(String status) {
        this.status = status;
        return this;
    }

    public OrderDTO transformToDto() {
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        if (this.orderDetails != null) {
            for (OrderDetail orderDetail : this.getOrderDetails()) {
                orderDetailDTOList.add(orderDetail.transformToDto());
            }
        }

        List<OrderStatusHistoryDTO> orderStatusHistoryDTOList = new ArrayList<>();
        if (this.orderStatusHistory != null) {
            for (OrderStatusHistory orderStatusHistory : this.getOrderStatusHistory()) {
                orderStatusHistoryDTOList.add(orderStatusHistory.transformToDto());
            }
        }

        return new OrderDTO(this.getOrderId(), this.getUserId(), this.getOrderDate(), this.getTotalAmount(), this.getStatus(),
                orderDetailDTOList, this.getShippingAddress(), this.getBillingAddress(), orderStatusHistoryDTOList, this.getUpdatedAt());
    }
}
