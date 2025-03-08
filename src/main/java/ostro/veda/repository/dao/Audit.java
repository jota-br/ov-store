package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import ostro.veda.model.dto.AuditDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor()
@Entity
@Table(name = "audits")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private int auditId;

    @Column(name = "action", length = 50, nullable = false)
    private String action;

    @Column(name = "changed_table", length = 50, nullable = false)
    private String changedTable;

    @Column(name = "changed_data", columnDefinition = "TEXT", nullable = false)
    private String changedData;

    @CreationTimestamp
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @ManyToOne
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;

    public Audit() {
    }

    public AuditDto transformToDto() {
        return new AuditDto(this.getAuditId(), this.getAction(), this.getChangedTable(),
                this.getChangedData(), this.getChangedAt(), this.getChangedBy().transformToDto(), this.getChangedBy().getUserId());
    }
}
