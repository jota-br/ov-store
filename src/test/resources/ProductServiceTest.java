package test.resources;

import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductImageRepository;
import ostro.veda.db.ProductRepository;
import ostro.veda.service.CategoryService;
import ostro.veda.service.EntityType;
import ostro.veda.service.ProductImageService;
import ostro.veda.service.ProductService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProductServiceTest {

    @Test
    public void processData() {
        /*
            Map<String, Boolean> images = k:Url v:isMain
         */

        List<String> categories = List.of("Furniture", "High quality handmade");
        Map<String, Boolean> images = Map.of("http://sub.example.co.uk/images/photo.png", true);

        try (ProductRepository productRepository = new ProductRepository();
             CategoryRepository categoryRepository = new CategoryRepository();
             ProductImageRepository productImageRepository = new ProductImageRepository()) {

            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);

            assertNull(productService.processData("Mega Box", "valid description",
                    -1, 15, true, categories, images, ProcessDataType.ADD, null));
            assertNull(productService.processData("Ultra Coin", "valid description",
                    0.0, -1, false, categories, images, ProcessDataType.ADD, null));


            assertNotNull(productService.processData("Ultra Chair", "valid description",
                    45.99, 15, false, categories, images, ProcessDataType.ADD, null));

            categories = List.of(
                    "Furniture", "High quality handmade", "Woodcraft", "Artisans work"
            );
            assertNotNull(productService.processData("New Ultra Chair", "New valid description",
                    49.99, 50, true, categories, images, ProcessDataType.UPDATE,
                    Map.of(EntityType.PRODUCT, 1)));

        } catch (Exception ignored) {
        }

    }
}
