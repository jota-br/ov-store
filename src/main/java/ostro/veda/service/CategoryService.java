package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.CategoryValidation;
import ostro.veda.db.CategoryRepository;
import ostro.veda.loggerService.Logger;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get Category List to be persisted. Called by ProductRepository.
     * @param categories category list to be validated.
     * @return List<CategoryDTO>
     * @throws ErrorHandling.InvalidInputException If validation is unsuccessful.
     */
    public List<CategoryDTO> addProduct(List<CategoryDTO> categories)
            throws ErrorHandling.InvalidInputException {

        getValidatedCategoryDTOList(categories);
        return categories.isEmpty() ? null : categories;
    }

    /**
     * Add Categories in the list.
     * @param categories Category list with categories to be persisted.
     * @return List<CategoryDTO>
     */
    public List<CategoryDTO> addCategory(List<CategoryDTO> categories) throws ErrorHandling.InvalidInputException {

        try {
            getValidatedCategoryDTOList(categories);
            if (categories.isEmpty()) return null;

            return categoryRepository.addCategory(categories);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    /**
     * Updates data in an existing Category.
     * @param categories Category list data to be updated with.
     * @return List<CategoryDTO>
     * @throws ErrorHandling.InvalidInputException if Input validation fails.
     */
    public List<CategoryDTO> updateCategory(List<CategoryDTO> categories)
            throws ErrorHandling.InvalidInputException {
        getValidatedCategoryDTOListWithId(categories);
        if (categories.isEmpty()) return null;
        return categoryRepository.updateCategory(categories);
    }

    /**
     * Validates the Category List Input.
     * @param categories Category list data to be validated.
     * @throws ErrorHandling.InvalidInputException If input validation fails.
     */
    private void getValidatedCategoryDTOListWithId(List<CategoryDTO> categories)
            throws ErrorHandling.InvalidInputException {

        for (CategoryDTO entity : categories) {
            int categoryId = entity.getCategoryId();
            String name = entity.getName();
            String description = entity.getDescription();

            ValidatedInput result = getHasValidInput(categoryId, name, description);
            if (result == null) categories.remove(entity);
        }
    }

    /**
     * Validates the Category List Input.
     * @param categories Category list data to be validated.
     * @throws ErrorHandling.InvalidInputException If input validation fails.
     */
    private void getValidatedCategoryDTOList(List<CategoryDTO> categories)
            throws ErrorHandling.InvalidInputException {

        for (CategoryDTO entity : categories) {
            int categoryId = entity.getCategoryId();
            String name = entity.getName();
            String description = entity.getDescription();

            ValidatedInput result = getHasValidInput(categoryId, name, description);
            if (result == null) categories.remove(entity);
        }
    }

    /**
     * Validation method.
     * @param categoryId
     * @param name
     * @param description
     * @return ValidatedInput
     * @throws ErrorHandling.InvalidInputException If validation fails.
     */
    private ValidatedInput getHasValidInput(int categoryId, String name, String description) throws ErrorHandling.InvalidInputException {
        if (!CategoryValidation.hasValidInput(categoryId, name, description)) return null;

        name = InputValidator.stringSanitize(name);
        description = InputValidator.stringSanitize(description);
        return new ValidatedInput(categoryId, name, description);
    }

    private record ValidatedInput(int categoryId, String name, String description) {
    }
}
