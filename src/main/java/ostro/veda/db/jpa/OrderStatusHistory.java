package ostro.veda.db.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import ostro.veda.common.dto.OrderStatusHistoryDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_status_history_id")
    private int orderStatusHistoryId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    public OrderStatusHistory() {
    }

    public OrderStatusHistory(Order order, String status) {
        this.order = order;
        this.status = status;
    }

    public int getOrderStatusHistoryId() {
        return orderStatusHistoryId;
    }

    public Order getOrder() {
        return order;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public OrderStatusHistoryDTO transformToDto() {
        return new OrderStatusHistoryDTO(this.getOrderStatusHistoryId(), null, this.getStatus(), this.getChangedAt());
    }
}
