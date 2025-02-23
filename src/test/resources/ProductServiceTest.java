package test.resources;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductImageRepository;
import ostro.veda.db.ProductRepository;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.service.CategoryService;
import ostro.veda.service.ProductImageService;
import ostro.veda.service.ProductService;

import java.util.Map;

import static org.junit.Assert.*;

public class ProductServiceTest {

    @Test
    public void addProduct() {
        Map<String, String> categories = Map.of("Furniture", "High quality handmade");
        Map<String, Boolean> images = Map.of("http://sub.example.co.uk/images/photo.png", true);

        EntityManager em = JPAUtil.getEm();
        try (ProductRepository productRepository = new ProductRepository(em);
             CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductImageRepository productImageRepository = new ProductImageRepository(em)) {

            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);

            assertNull(productService.addProduct("Mega Box", "valid description",
                    -1, 15, true, categories, images));
            assertNull(productService.addProduct("Ultra Coin", "valid description",
                    0.0, -1, false, categories, images));


            assertNotNull(productService.addProduct("Ultra Chair", "valid description",
                    45.99, 15, false, categories, images));

            categories = Map.of(
                    "Furniture", "High quality handmade", "Woodcraft", "Artisans work"
            );
            assertNotNull(productService.addProduct("New Ultra Chair", "New valid description",
                    49.99, 50, true, categories, images));

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }
}
