package test.resources;

import org.junit.Test;
import ostro.veda.service.ProductService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProductServiceTest {

    @Test
    public void processData() {
        assertNull(ProductService.processData("four", "valid description", 45.99, 15));
        assertNull(ProductService.processData("Mega Box", "valid description", -1, 15));
        assertNull(ProductService.processData("Ultra Coin", "valid description", 0.0, -1));

        assertNotNull(ProductService.processData("Ultra Chair", "valid description", 45.99, 15));
    }
}
