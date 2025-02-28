package ostro.veda.service;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
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
    public CategoryDTO addCategory(CategoryDTO category) {
        try {
            ValidateUtil.validateCategory(category);
            category = SanitizeUtil.sanitizeCategory(category);
            return categoryRepository.addCategory(category);
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
    public CategoryDTO updateCategory(CategoryDTO categories) {
        try {
            ValidateUtil.validateCategory(categories);
            categories = SanitizeUtil.sanitizeCategory(categories);
            return categoryRepository.updateCategory(categories);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
