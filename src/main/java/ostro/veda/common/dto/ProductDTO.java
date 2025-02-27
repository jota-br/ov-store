package ostro.veda.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final int version;

    /**
     *
     * @param productId int
     * @param name String
     * @param description String
     * @param price double
     * @param stock int
     * @param isActive boolean
     * @param categories List CategoryDTO
     * @param images List ProductImageDTO
     * @param createdAt LocalDateTime
     * @param updatedAt LocalDateTime
     * @param version Optimistic Locking
     */
    public ProductDTO(int productId, String name, String description, double price, int stock, boolean isActive,
                      List<CategoryDTO> categories, List<ProductImageDTO> images, LocalDateTime createdAt, LocalDateTime updatedAt,
                      int version) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.isActive = isActive;
        this.categories = categories == null ? new ArrayList<>() : categories;
        this.images = images == null ? new ArrayList<>() : images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    /**
     *
     * @param productId int
     * @param name String
     * @param description String
     * @param price double
     * @param stock int
     * @param isActive boolean
     * @param categories List CategoryDTO
     * @param images List ProductImageDTO
     */
    public ProductDTO(int productId, String name, String description, double price, int stock, boolean isActive,
                      List<CategoryDTO> categories, List<ProductImageDTO> images) {
        this(productId, name, description, price, stock, isActive, categories, images, null, null, 0);
    }

    /**
     *
     * @param name String
     * @param description String
     * @param price double
     * @param stock int
     * @param isActive boolean
     * @param categories List CategoryDTO
     * @param images List ProductImageDTO
     */
    public ProductDTO(String name, String description, double price, int stock, boolean isActive,
                      List<CategoryDTO> categories, List<ProductImageDTO> images) {
        this(0, name, description, price, stock, isActive, categories, images, null, null, 0);
    }

    // GETTERS
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

    public int getVersion() {
        return version;
    }
}
