package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.CategoryRepository;

@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO addCategory(CategoryDTO category) {
        try {
            ValidateUtil.validateCategory(category);
            category = SanitizeUtil.sanitizeCategory(category);
            return categoryRepository.addCategory(category);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public CategoryDTO updateCategory(CategoryDTO categories) {
        try {
            ValidateUtil.validateCategory(categories);
            categories = SanitizeUtil.sanitizeCategory(categories);
            return categoryRepository.updateCategory(categories);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
