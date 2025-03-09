package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ostro.veda.model.dto.OrderStatusHistoryDto;
import ostro.veda.util.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_status_history_id")
    private int orderStatusHistoryId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @CreationTimestamp
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @Version
    private int version;

    public OrderStatusHistory() {
    }

    public OrderStatusHistoryDto transformToDto() {
        return new OrderStatusHistoryDto(this.getOrderStatusHistoryId(), null, this.getStatus(),
                this.getChangedAt(), this.getVersion());
    }
}
