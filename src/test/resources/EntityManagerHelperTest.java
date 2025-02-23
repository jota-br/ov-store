package test.resources;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductImageRepository;
import ostro.veda.db.ProductRepository;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.ProductColumns;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.CategoryService;
import ostro.veda.service.ProductImageService;
import ostro.veda.service.ProductService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EntityManagerHelperTest {

    @Test
    public void findByFields() {

        EntityManagerHelper entityManagerHelper = EntityManagerHelper.getEntityManagerHelper();
        EntityManager em = JPAUtil.getEm();
        try (ProductRepository productRepository = new ProductRepository(em);
             CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductImageRepository productImageRepository = new ProductImageRepository(em)) {

            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);

            Map<String, String> categories = Map.of("Furniture", "High Quality Hand Made");
            Map<String, Boolean> images = Map.of("http://sub.example.co.uk/images/photo.png", true);

            String name = "Ultra Chair Classic";
            String description = "Valid Product Description";
            double price = 45.99;
            int stock = 15;

            ProductDTO productDTO = productService.addProduct(name, description, price, stock, true,
                    categories, images);

            List<Product> product = entityManagerHelper.findByFields(productRepository.getEm(), Product.class,
                    Map.of(
                            ProductColumns.NAME.getColumnName(), name,
                            ProductColumns.DESCRIPTION.getColumnName(), description,
                            ProductColumns.PRICE.getColumnName(), String.valueOf(price),
                            ProductColumns.STOCK.getColumnName(), String.valueOf(stock)
                    ));

            assertNotNull(product);
            assertNotNull(productDTO);
            assertEquals(productDTO.getName(), product.get(0).getName());
            assertEquals(productDTO.getDescription(), product.get(0).getDescription());
            assertEquals(productDTO.getPrice(), product.get(0).getPrice(), 0);
            assertEquals(productDTO.getStock(), product.get(0).getStock());
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }
}