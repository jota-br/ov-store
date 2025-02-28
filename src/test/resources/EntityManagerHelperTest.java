package test.resources;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductRepository;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.ProductColumns;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.ProductService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EntityManagerHelperTest {

    @Test
    public void findByFields() {

        ResetTables.resetTables();

        EntityManagerHelper entityManagerHelper = EntityManagerHelper.getEntityManagerHelper();
        EntityManager em = JPAUtil.getEm();
        try (CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductRepository productRepository = new ProductRepository(em, categoryRepository);) {

            ProductService productService = new ProductService(productRepository);

            List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
            List<ProductImageDTO> images = TestHelper.getProductImageDTOS();

            String name = "Ultra Chair Classic";
            String description = "Valid Product Description";
            double price = 45.99;
            int stock = 15;

            ProductDTO productDTO = productService.addProduct(new ProductDTO(name, description, price, stock, true,
                    categories, images));

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
        }
    }
}