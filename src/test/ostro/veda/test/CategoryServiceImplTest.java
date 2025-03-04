package test.ostro.veda.test;

import main.java.ostro.veda.common.dto.CategoryDTO;
import main.java.ostro.veda.config.AppConfig;
import main.java.ostro.veda.service.CategoryServiceImpl;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertNotNull;

public class CategoryServiceImplTest {

    private CategoryDTO getCategoryDTO() {
        return new CategoryDTO(0, "Category Name", "Category Description",
                true, null, null, 0);
    }

    private CategoryDTO getCategoryDTOWithId(int id) {
        return new CategoryDTO(id, "Furniture", "Furniture Hand Made Products",
                true, null, null, 0);
    }

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        CategoryServiceImpl categoryService = context.getBean(CategoryServiceImpl.class);

        CategoryDTO categoryDTO = categoryService.add(getCategoryDTO());
        assertNotNull(categoryDTO);

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        CategoryServiceImpl categoryService = context.getBean(CategoryServiceImpl.class);

        CategoryDTO categoryDTO = categoryService.add(getCategoryDTO());
        categoryDTO = categoryService.update(getCategoryDTOWithId(categoryDTO.getCategoryId()));
        assertNotNull(categoryDTO);

        context.close();
    }
}