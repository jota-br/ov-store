package test.resources;

import org.junit.Test;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.db.jpa.Category;
import ostro.veda.service.CategoryService;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CategoryServiceTest {

    @Test
    public void addCategory() {
        try (CategoryRepository categoryRepository = new CategoryRepository()) {

            CategoryService categoryService = new CategoryService(categoryRepository);

            List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
            assertNotNull(categoryService.addCategory(categories));

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }

    @Test
    public void updateCategory() {
        try (CategoryRepository categoryRepository = new CategoryRepository()) {

            CategoryService categoryService = new CategoryService(categoryRepository);

            List<CategoryDTO> categories = TestHelper.getCategoryDTOS();
            List<CategoryDTO> categoryDTO = categoryService.addCategory(categories);

            List<Category> category = categoryRepository.getEntityManagerHelper().findByFields(categoryRepository.getEm(), Category.class,
                    Map.of("name", categoryDTO.get(0).getName()));
            categories.clear();
            categories.add(new CategoryDTO(category.get(0).getCategoryId(), "New Name Modified", "New Description", false));
            assertNotNull(categoryService.updateCategory(categories));

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }
}