package ostro.veda.db.jpa;

import jakarta.persistence.*;
import ostro.veda.common.dto.ProductImageDTO;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private int productImageId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_main")
    private boolean isMain;

    public ProductImage() {
    }

    public ProductImage(String imageUrl, boolean isMain) {
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }

    public int getProductImageId() {
        return productImageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isMain() {
        return isMain;
    }

    public ProductImage updateProductImage(ProductImage updatedData) {
        this.imageUrl = updatedData.getImageUrl();
        this.isMain = updatedData.isMain();

        return this;
    }

    public ProductImageDTO transformToDto() {
        return new ProductImageDTO(this.getProductImageId(), this.getImageUrl(), this.isMain());
    }
}
