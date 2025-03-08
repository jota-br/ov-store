package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.model.dto.PermissionDto;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int permissionId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 155)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    public Permission() {
    }

    public PermissionDto transformToDto() {
        return new PermissionDto(this.getPermissionId(), this.getName(), this.getDescription(),
                this.getCreatedAt(), this.getUpdatedAt(), this.getVersion());
    }
}
