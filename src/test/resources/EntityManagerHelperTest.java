package test.resources;

import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductImageRepository;
import ostro.veda.db.ProductRepository;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.columns.ProductColumns;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.CategoryService;
import ostro.veda.service.ProductImageService;
import ostro.veda.service.ProductService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EntityManagerHelperTest {

    @Test
    public void findByFields() {

        EntityManagerHelper entityManagerHelper = new EntityManagerHelper();
        try (ProductRepository productRepository = new ProductRepository(entityManagerHelper);
             CategoryRepository categoryRepository = new CategoryRepository(entityManagerHelper);
             ProductImageRepository productImageRepository = new ProductImageRepository(entityManagerHelper)) {

            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);

            List<String> categories = List.of("Furniture");
            Map<String, Boolean> images = Map.of("http://sub.example.co.uk/images/photo.png", true);

            String name = "Ultra Chair Classic";
            String description = "Valid Product Description";
            double price = 45.99;
            int stock = 15;

            ProductDTO productDTO = productService.processData(name, description, price, stock, true,
                    categories, images, ProcessDataType.ADD, null);

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
        } catch (Exception ignored) {
        }
    }
}