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
            dto = new ProductImageDTO(-1, url, isMain);
        }
        return dto;
    }

    public ProductImageDTO updateImage(int productImageId, String url, boolean isMain) {

        ProductImage productImage = this.em.find(ProductImage.class, productImageId);

        if (productImage == null) {
            return addImage(url, isMain);
        }

        productImage.updateProductImage(new ProductImage(url, isMain));

        boolean isInserted = this.entityManagerHelper.executeMerge(this.em, productImage);
        if (!isInserted) {
            return null;
        }

        return productImage.transformToDto();
    }
}
