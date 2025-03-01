package test.ostro.veda.test;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductRepository;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.service.ProductService;

import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceTest {

    @Test
    public void addProduct() {

        ResetTables.resetTables();
        EntityManager em = JPAUtil.getEm();
        try (CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductRepository productRepository = new ProductRepository(em, categoryRepository)) {

            ProductService productService = new ProductService(productRepository);

            List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
            List<ProductImageDTO> images = TestHelper.getProductImageDTOS();

            categories.add(new CategoryDTO(categories.get(0).getCategoryId(), "New Name Modified",
                    "New Description", false, null, null, 0));
            categories.remove(0);

            assertNotNull(productService.addProduct(new ProductDTO(0, "New Ultra Chair", "New valid description",
                    49.99, 50, true, categories, images, null, null, 0)));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateProduct() {

        ResetTables.resetTables();
        List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
        List<ProductImageDTO> images = TestHelper.getProductImageDTOS();

        EntityManager em = JPAUtil.getEm();
        try (CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductRepository productRepository = new ProductRepository(em, categoryRepository)) {

            ProductService productService = new ProductService(productRepository);

            ProductDTO productDTO = productService.addProduct(new ProductDTO(0, "New Ultra Chair", "New valid description",
                    949.99, 1, true, categories, images, null, null, 0));

            categories.add(new CategoryDTO(categories.get(0).getCategoryId(), "New Name Modified",
                    "New Description", false, null, null, 0));
            categories.remove(0);

            images.add(new ProductImageDTO(images.get(0).getProductImageId(), "http://sub.example.co.uk/images/photo2.png", true, 0));
            images.remove(0);

            assertNotNull(productService.updateProduct(new ProductDTO(productDTO.getProductId(), "Mahogany Chair",
                    "Exceptional hand made quality in Mahogany wood",
                    949.99, 2, true, categories, images, null, null, 0)));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
