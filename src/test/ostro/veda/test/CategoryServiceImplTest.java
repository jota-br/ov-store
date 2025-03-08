package ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.model.dto.CategoryDto;
import ostro.veda.config.AppConfig;
import ostro.veda.service.CategoryServiceImpl;

import static org.junit.Assert.assertNotNull;

public class CategoryServiceImplTest {

    private static Helper helper = new Helper();

    @Test
    public void add() {

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        CategoryServiceImpl categoryService = context.getBean(CategoryServiceImpl.class);

        CategoryDto categoryDTO = categoryService.add(helper.getCategoryDTO());
        assertNotNull(categoryDTO);

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        CategoryServiceImpl categoryService = context.getBean(CategoryServiceImpl.class);

        CategoryDto categoryDTO = categoryService.add(helper.getCategoryDTO());
        categoryDTO = categoryService.update(helper.getCategoryDTOWithId(categoryDTO.getCategoryId()));
        assertNotNull(categoryDTO);

        context.close();
    }
}