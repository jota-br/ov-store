package test.resources;

import org.junit.Test;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.CategoryRepository;
import ostro.veda.service.CategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CategoryServiceTest {

    @Test
    public void addCategory() {
        try (CategoryRepository categoryRepository = new CategoryRepository()) {

            CategoryService categoryService = new CategoryService(categoryRepository);

            Map<String, String> categories = Map.of(
                    "Furniture", "High quality handmade", "Woodcraft", "Artisans work"
            );
            assertNotNull(categoryService.addCategory(categories, List.of(true, true)));

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

            Map<String, String> categories = Map.of(
                    "Furniture", "High quality handmade", "Woodcraft", "Artisans work"
            );
            List<CategoryDTO> categoryDTO = categoryService.addCategory(categories, List.of(true, true));

            Map<Integer, List<Map<String, String>>> idNameAndDescription = new HashMap<>();
            List<Map<String, String>> list = new ArrayList<>();
            list.add(Map.of("Furnitures", "handmade"));
            idNameAndDescription.put(categoryDTO.get(0).getCategoryId(), list);
            assertNotNull(categoryService.updateCategory(idNameAndDescription, List.of(true, true)));

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }
}