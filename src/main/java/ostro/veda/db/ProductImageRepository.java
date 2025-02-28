package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.helpers.columns.ProductImageColumns;
import ostro.veda.db.jpa.ProductImage;

import java.util.List;
import java.util.Map;

public class ProductImageRepository extends Repository {

    public ProductImageRepository(EntityManager em) {
        super(em);
    }

    public ProductImageDTO addImage(String url, boolean isMain) {

        List<ProductImage> result = this.entityManagerHelper.findByFields(this.em, ProductImage.class,
                Map.of(ProductImageColumns.IMAGE_URL.getColumnName(), url));
        ProductImage productImage = null;
        if (result != null && !result.isEmpty()) {
            productImage = result.get(0);
        }

        ProductImageDTO dto = null;
        if (productImage != null) {
            dto = productImage.transformToDto();
        } else {
            dto = new ProductImageDTO(0, url, isMain);
        }
        return dto;
    }
}
