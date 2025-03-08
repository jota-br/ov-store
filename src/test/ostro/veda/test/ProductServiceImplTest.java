package ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.model.dto.ProductDto;
import ostro.veda.config.AppConfig;
import ostro.veda.service.ProductServiceImpl;

public class ProductServiceImplTest {

    private static Helper helper = new Helper();

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);

        productService.add(helper.getProductDTO());

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);

        ProductDto productDTO = productService.add(helper.getProductDTO());
        productService.update(helper.getProductDTOWithId(productDTO.getProductId()));

        context.close();
    }
}