package test.resources;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.ProductImageRepository;
import ostro.veda.db.ProductRepository;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.service.CategoryService;
import ostro.veda.service.ProductImageService;
import ostro.veda.service.ProductService;

import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceTest {

    @Test
    public void addProduct() {
        EntityManager em = JPAUtil.getEm();
        try (CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductRepository productRepository = new ProductRepository(em, categoryRepository);
             ProductImageRepository productImageRepository = new ProductImageRepository(em)) {

            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);

            List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
            List<ProductImageDTO> images = TestHelper.getProductImageDTOS();

            assertNull(productService.addProduct("Mega Box", "valid description",
                    -1, 15, true, categories, images));
            assertNull(productService.addProduct("Ultra Coin", "valid description",
                    0.0, -1, false, categories, images));


            assertNotNull(productService.addProduct("Ultra Chair", "valid description",
                    45.99, 15, false, categories, images));

            categories.add(new CategoryDTO(categories.get(0).getCategoryId(), "New Name Modified", "New Description", false));
            categories.remove(0);

            assertNotNull(productService.addProduct("New Ultra Chair", "New valid description",
                    49.99, 50, true, categories, images));

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }

    @Test
    public void updateProduct() {
        List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
        List<ProductImageDTO> images = TestHelper.getProductImageDTOS();

        EntityManager em = JPAUtil.getEm();
        try (CategoryRepository categoryRepository = new CategoryRepository(em);
             ProductRepository productRepository = new ProductRepository(em, categoryRepository);
             ProductImageRepository productImageRepository = new ProductImageRepository(em)) {

            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);

            ProductDTO productDTO = productService.addProduct("New Ultra Chair", "New valid description",
                    949.99, 1, true, categories, images);

            categories.add(new CategoryDTO(categories.get(0).getCategoryId(), "New Name Modified", "New Description", false));
            categories.remove(0);

            images.add(new ProductImageDTO(images.get(0).getProductImageId(), "http://sub.example.co.uk/images/photo2.png", true));
            images.remove(0);

            assertNotNull(productService.updateProduct(productDTO.getProductId(), "Mahogany Chair",
                    "Exceptional hand made quality in Mahogany wood",
                    949.99, 2, true, categories, images));

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }
}
