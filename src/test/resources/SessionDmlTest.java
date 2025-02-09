package test.resources;

import org.hibernate.Session;
import org.junit.Test;
import ostro.veda.db.DbConnection;
import ostro.veda.db.helpers.SessionDml;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.ProductService;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class SessionDmlTest {

    @Test
    public void findByFields() {
        ProductService.processData("Ultra Chair", "valid description", 45.99, 15);
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