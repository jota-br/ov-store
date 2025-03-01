package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.CategoryDTO;

import java.time.LocalDateTime;


@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 510)
    private String description;

    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    public Category() {
    }

    public Category updateCategory(Category updatedData) {
        this.name = updatedData.getName();
        this.description = updatedData.getDescription();
        this.isActive = updatedData.isActive();

        return this;
    }

    public CategoryDTO transformToDto() {
        return new CategoryDTO(this.getCategoryId(), this.getName(), this.getDescription(),
                this.isActive(), this.getCreatedAt(), this.getUpdatedAt(), this.getVersion());
    }
}
