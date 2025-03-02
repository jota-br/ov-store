package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.OrderStatusHistoryDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "address_id")
    private Address shippingAddress;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "address_id")
    private Address billingAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<OrderStatusHistory> orderStatusHistory;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    public Order() {
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

        return new OrderDTO(this.getOrderId(), this.getUserId(),
                this.getOrderDate(), this.getTotalAmount(),
                this.getStatus(), orderDetailDTOList,
                this.getShippingAddress().transformToDto(), this.getBillingAddress().transformToDto(),
                orderStatusHistoryDTOList, this.getUpdatedAt(),
                this.getVersion());
    }
}
