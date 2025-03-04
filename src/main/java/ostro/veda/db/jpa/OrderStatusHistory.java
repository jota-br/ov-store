package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ostro.veda.common.dto.OrderStatusHistoryDTO;

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

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @Version
    private int version;

    public OrderStatusHistory() {
    }

    public OrderStatusHistoryDTO transformToDto() {
        return new OrderStatusHistoryDTO(this.getOrderStatusHistoryId(), null, this.getStatus(),
                this.getChangedAt(), this.getVersion());
    }
}
