package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ostro.veda.common.dto.ProductImageDTO;

@Getter
@Setter
@Accessors(chain = true)
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

    public ProductImageDTO transformToDto() {
        return new ProductImageDTO(this.getProductImageId(), this.getImageUrl(), this.isMain(), this.getVersion());
    }
}
