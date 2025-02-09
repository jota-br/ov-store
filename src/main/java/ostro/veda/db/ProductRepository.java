package ostro.veda.db;

import org.hibernate.Session;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.db.helpers.SessionDml;
import ostro.veda.db.jpa.Product;

import java.util.List;
import java.util.Map;

public class ProductRepository {

    public static ProductDTO addProduct(String name, String description, double price, int stock) {

        Session session = DbConnection.getOpenSession();
        List<Product> result = SessionDml.findByFields(session, Product.class, Map.of("name", name));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        Product product = new Product(name, description, price, stock);
        boolean isInserted = SessionDml.executePersist(session, product);
        if (!isInserted) {
            return null;
        }

        ProductDTO dto = product.transformToDto();
        DbConnection.closeSession(session);

        return dto;
    }
}
