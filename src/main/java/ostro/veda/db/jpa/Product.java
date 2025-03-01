package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 510)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "products_images",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_image_id")
    )
    private List<ProductImage> images = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    public Product() {
    }

    public Product updateProduct(Product updatedData) {
        this.name = updatedData.getName();
        this.description = updatedData.getDescription();
        this.price = updatedData.getPrice();
        this.stock = updatedData.getStock();
        this.isActive = updatedData.isActive();
        this.categories = updatedData.getCategories();
        this.images = updatedData.getImages();

        return this;
    }

    public Product updateStock(int newStock) {
        this.stock = newStock;
        return this;
    }

    public ProductDTO transformToDto() {
        List<ProductImageDTO> images = new ArrayList<>();
        if (this.images != null) {
            for (ProductImage pi : this.getImages()) {
                if (pi == null) continue;
                images.add(pi.transformToDto());
            }
        }

        List<CategoryDTO> categories = new ArrayList<>();
        if (this.categories != null) {
            for (Category c : this.getCategories()) {
                if (c == null) continue;
                categories.add(c.transformToDto());
            }
        }

        return new ProductDTO(this.getProductId(), this.getName(), this.getDescription(), this.getPrice(), this.getStock(), this.isActive(),
                categories, images, this.getCreatedAt(), this.getUpdatedAt(), this.getVersion());
    }
}
