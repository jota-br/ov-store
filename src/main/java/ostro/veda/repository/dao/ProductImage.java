package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ostro.veda.model.dto.ProductImageDto;

@Getter
@Builder
@AllArgsConstructor
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

    @Version
    private int version;

    public ProductImage() {
    }

    public ProductImage updateProductImage(ProductImage updatedData) {
        this.imageUrl = updatedData.getImageUrl();
        this.isMain = updatedData.isMain();

        return this;
    }

    public ProductImageDto transformToDto() {
        return new ProductImageDto(this.getProductImageId(), this.getImageUrl(), this.isMain(), this.getVersion());
    }
}
