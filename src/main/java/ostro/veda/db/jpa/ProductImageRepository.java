package ostro.veda.db.jpa;

import org.hibernate.Session;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.DbConnection;
import ostro.veda.db.helpers.SessionDml;

import java.util.List;
import java.util.Map;

public class ProductImageRepository {

    public static ProductImageDTO addImage(String url, boolean isMain) {

        Session session = DbConnection.getOpenSession();
        List<ProductImage> result = SessionDml.findByFields(session, ProductImage.class, Map.of("imageUrl", url));
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
        DbConnection.closeSession(session);

        return dto;
    }
}
