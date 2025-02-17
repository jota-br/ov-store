package ostro.veda.db.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Product() {
    }

    @Version
    private int version;

    public Product(String name, String description, double price, int stock, boolean isActive) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.isActive = isActive;
    }

    public Product(String name, String description, double price, int stock, boolean isActive, List<Category> categories, List<ProductImage> images) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.isActive = isActive;
        this.categories = categories;
        this.images = images;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getVersion() {
        return version;
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
                images.add(pi.transformToDto());
            }
        }

        List<CategoryDTO> categories = new ArrayList<>();
        if (this.categories != null) {
            for (Category c : this.getCategories()) {
                categories.add(c.transformToDto());
            }
        }

        return new ProductDTO(this.getProductId(), this.getName(), this.getDescription(), this.getPrice(), this.getStock(), this.isActive(),
                categories, images, this.getCreatedAt(), this.getUpdatedAt(), this.getVersion());
    }
}
