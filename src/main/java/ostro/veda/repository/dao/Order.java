package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.model.dto.CouponDto;
import ostro.veda.model.dto.OrderDto;
import ostro.veda.model.dto.OrderDetailDto;
import ostro.veda.model.dto.OrderStatusHistoryDto;
import ostro.veda.util.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Setter
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "address_id")
    private Address shippingAddress;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "address_id")
    private Address billingAddress;

    @Setter
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<OrderStatusHistory> orderStatusHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    public Order() {
    }

    public Order updateOrderStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderDto transformToDto() {
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        if (this.orderDetails != null) {
            for (OrderDetail orderDetail : this.getOrderDetails()) {
                orderDetailDtoList.add(orderDetail.transformToDto());
            }
        }

        List<OrderStatusHistoryDto> orderStatusHistoryDtoList = new ArrayList<>();
        if (this.orderStatusHistory != null) {
            for (OrderStatusHistory orderStatusHistory : this.getOrderStatusHistory()) {
                orderStatusHistoryDtoList.add(orderStatusHistory.transformToDto());
            }
        }

        CouponDto couponDTO = this.getCoupon() != null ? this.getCoupon().transformToDto() : null;

        return new OrderDto(this.getOrderId(), this.getUserId(),
                this.getOrderDate(), this.getTotalAmount(),
                this.getStatus(), orderDetailDtoList,
                this.getShippingAddress().transformToDto(), this.getBillingAddress().transformToDto(),
                orderStatusHistoryDtoList, couponDTO,
                this.getUpdatedAt(), this.getVersion());
    }
}
