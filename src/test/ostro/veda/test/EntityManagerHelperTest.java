package test.ostro.veda.test;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductRepositoryImpl;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.ProductColumns;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.ProductServiceImpl;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EntityManagerHelperTest {

    @Test
    public void findByFields() {

        ResetTables.resetTables();

        EntityManagerHelper entityManagerHelper = new EntityManagerHelper();
        EntityManager em = JPAUtil.getEm();
        try (CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl(em, categoryRepository);) {

            ProductServiceImpl productServiceImpl = new ProductServiceImpl(productRepositoryImpl);

            List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
            List<ProductImageDTO> images = TestHelper.getProductImageDTOS();

            String name = "Ultra Chair Classic";
            String description = "Valid Product Description";
            double price = 45.99;
            int stock = 15;

            ProductDTO productDTO = productServiceImpl.addProduct(new ProductDTO(0, name, description, price, stock, true,
                    categories, images, null, null, 0));

            List<Product> product = entityManagerHelper.findByFields(productRepositoryImpl.getEm(), Product.class,
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