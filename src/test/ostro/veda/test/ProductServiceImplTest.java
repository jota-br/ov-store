package test.ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.config.AppConfig;
import ostro.veda.service.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImplTest {

    private ProductDTO getProductDTO() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        CategoryDTO categoryDTO = new CategoryDTO(0, "Category Name", "Category Description",
                true, null, null, 0);
        categoryDTOS.add(categoryDTO);

        List<ProductImageDTO> productImageDTOS = new ArrayList<>();
        ProductImageDTO productImageDTO = new ProductImageDTO(0,
                "https://imagesemxaple.com/image.png", true, 0);
        productImageDTOS.add(productImageDTO);

        return new ProductDTO(0, "Product One", "Description One",
                99.99,10, true, categoryDTOS, productImageDTOS, null, null, 0);
    }

    private ProductDTO getProductDTOWithId(int id) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        CategoryDTO categoryDTO = new CategoryDTO(0, "Category Name", "Category Description",
                true, null, null, 0);
        categoryDTOS.add(categoryDTO);

        List<ProductImageDTO> productImageDTOS = new ArrayList<>();
        ProductImageDTO productImageDTO = new ProductImageDTO(0,
                "https://imagesemxaple.com/image.png", true, 0);
        productImageDTOS.add(productImageDTO);

        return new ProductDTO(id, "Product Two", "Description Two",
                199.99,20, true, categoryDTOS, productImageDTOS, null, null, 0);
    }

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);

        productService.add(getProductDTO());

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);

        ProductDTO productDTO = productService.add(getProductDTO());
        productService.update(getProductDTOWithId(productDTO.getProductId()));

        context.close();
    }
}