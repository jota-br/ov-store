package ostro.veda.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDTO {

    private final int productId;
    private final String name;
    private final String description;
    private final double price;
    private final int stock;
    private final boolean isActive;
    private final List<CategoryDTO> categories;
    private final List<ProductImageDTO> images;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ProductDTO(int productId, String name, String description, double price, int stock, boolean isActive,
                      List<CategoryDTO> categories, List<ProductImageDTO> images, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.isActive = isActive;
        this.categories = categories;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public List<ProductImageDTO> getImages() {
        return images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
