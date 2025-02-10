package test.resources;

import org.hibernate.Session;
import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.DbConnection;
import ostro.veda.db.helpers.SessionDml;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class SessionDmlTest {

    @Test
    public void findByFields() {
        List<CategoryDTO> categories = List.of(new CategoryDTO(1, "name of category", "this is the description", true,
                LocalDateTime.now(), LocalDateTime.now()));
        List<ProductImageDTO > images = List.of(new ProductImageDTO(1, "http://sub.example.co.uk/images/photo.png", true));

        ProductService.processData(null,"Ultra Chair", "valid description", 45.99, 15, true,
                categories, images, ProcessDataType.ADD);

        Session session = DbConnection.getOpenSession();
        assertNotNull(SessionDml.findByFields(session, Product.class,
                Map.of(
                        "name", "Ultra Chair",
                        "description", "valid description",
                        "price", "45.99",
                        "stock", "15"
                )));
        DbConnection.closeSession(session);
    }
}