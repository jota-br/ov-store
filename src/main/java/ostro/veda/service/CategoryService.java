package ostro.veda.service;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.validation.ProductValidation;
import ostro.veda.db.CategoryRepository;
import ostro.veda.loggerService.Logger;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Add Categories in the list.
     * @param categories Category list with categories to be persisted.
     * @return List with CategoryDTO.
     */
    public List<CategoryDTO> addCategory(List<CategoryDTO> categories) {
        try {
            return categoryRepository.addCategory(ProductValidation.validateAndSanitizeCategories(categories));
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    /**
     * Updates data in an existing Category.
     * @param categories List with CategoryDTO.
     * @return List with CategoryDTO.
     */
    public List<CategoryDTO> updateCategory(List<CategoryDTO> categories) {
        try {
            return categoryRepository.updateCategory(ProductValidation.validateAndSanitizeCategories(categories));
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
