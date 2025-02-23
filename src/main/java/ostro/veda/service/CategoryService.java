package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.CategoryValidation;
import ostro.veda.db.CategoryRepository;
import ostro.veda.loggerService.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Called by addProduct in ProductService class to validate Category data
     * the validation is passed to CategoryService and the Category after validated
     * - if validated - will be persisted with the Product and ProductImage in the
     * ProductRepository class.
     *
     * @param categoryAndDescription Map contains key with the Category Name
     *                               and Value with Category Description.
     * @param isActive               Defines if a Category is active (true) or inactive (false).
     * @return returns List with CategoryDTO entity if Input is valid.
     * @throws ErrorHandling.InvalidInputException is thrown when input is invalid.
     */
    public List<CategoryDTO> addProduct(Map<String, String> categoryAndDescription, boolean isActive)
            throws ErrorHandling.InvalidInputException {

        List<CategoryDTO> categoryDTOList = getValidatedCategoryDTOList(categoryAndDescription, isActive);
        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }

    /**
     * Validates Category Input before persisting to the Database.
     * This will call - on valid input - CategoryRepository to persist data.
     *
     * @param categoryAndDescription Map contains key with the Category Name
     *                               and Value with Category Description.
     * @param isActive               Defines if a Category is active (true) or inactive (false).
     * @return returns List with CategoryDTO entity if Input is valid.
     */
    public List<CategoryDTO> addCategory(Map<String, String> categoryAndDescription, List<Boolean> isActive) {

        try {
            List<CategoryDTO> categoryDTOList = getValidatedCategoryDTOList(categoryAndDescription, isActive);
            if (categoryDTOList.isEmpty()) return null;

            return categoryRepository.addCategory(categoryAndDescription, isActive);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    /**
     * Updates Category with new data.
     * @param idNameAndDescription Map with id of the Category and List with Map containing
     *                             Name and Description.
     * @param isActive true if active, false if inactive
     * @return returns List with updated CategoryDTO on success or null.
     * @throws ErrorHandling.InvalidInputException If Input is invalid.
     */
    public List<CategoryDTO> updateCategory(Map<Integer, List<Map<String, String>>> idNameAndDescription, List<Boolean> isActive)
            throws ErrorHandling.InvalidInputException {
        List<CategoryDTO> categoryDTOList = getValidatedCategoryDTOListWithId(idNameAndDescription, isActive);
        if (categoryDTOList == null || categoryDTOList.isEmpty()) return null;
        return categoryRepository.updateCategory(idNameAndDescription, isActive);
    }

    /**
     * Validates Category Input
     *
     * @param idNameAndDescription Map contains key with the Category Name
     *                               and Value with Category Description.
     * @param isActive               Defines if a Category is active (true) or inactive (false).
     * @return returns List with CategoryDTO entity if Input is valid.
     * @throws ErrorHandling.InvalidInputException is thrown when input is invalid.
     */
    private List<CategoryDTO> getValidatedCategoryDTOListWithId(Map<Integer, List<Map<String, String>>> idNameAndDescription, List<Boolean> isActive)
            throws ErrorHandling.InvalidInputException {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        int i = 0;
        for (Map.Entry<Integer, List<Map<String, String>>> entrySet : idNameAndDescription.entrySet()) {
            int categoryId = entrySet.getKey();
            for (Map<String, String> e : entrySet.getValue()) {
                for (Map.Entry<String, String> entry : e.entrySet()) {

                    String name = entry.getKey();
                    String description = entry.getValue();

                    hasValidInput result = getHasValidInput(name, description);
                    if (result == null) continue;

                    categoryDTOList.add(new CategoryDTO(categoryId, result.name(), result.description(), isActive.get(i),
                            null, null));
                }
            }
            i++;
        }
        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }

    /**
     * Validates Category Input
     *
     * @param categoryAndDescription Map contains key with the Category Name
     *                               and Value with Category Description.
     * @param isActive               Defines if a Category is active (true) or inactive (false).
     * @return returns List with CategoryDTO entity if Input is valid.
     * @throws ErrorHandling.InvalidInputException is thrown when input is invalid.
     */
    private List<CategoryDTO> getValidatedCategoryDTOList(Map<String, String> categoryAndDescription, List<Boolean> isActive) throws ErrorHandling.InvalidInputException {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        int i = 0;

        for (Map.Entry<String, String> entry : categoryAndDescription.entrySet()) {
            String name = entry.getKey();
            String description = entry.getValue();
            boolean active = isActive.get(i);

            hasValidInput result = getHasValidInput(name, description);
            if (result == null) continue;

            categoryDTOList.add(new CategoryDTO(-1, result.name(), result.description(), active, null, null));
            i++;
        }
        return categoryDTOList;
    }

    /**
     * Validates Category Input
     *
     * @param categoryAndDescription Map contains key with the Category Name
     *                               and Value with Category Description.
     * @param isActive               Defines if a Category is active (true) or inactive (false).
     * @return returns List with CategoryDTO entity if Input is valid.
     * @throws ErrorHandling.InvalidInputException is thrown when input is invalid.
     */
    private List<CategoryDTO> getValidatedCategoryDTOList(Map<String, String> categoryAndDescription, boolean isActive)
            throws ErrorHandling.InvalidInputException {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (Map.Entry<String, String> entry : categoryAndDescription.entrySet()) {
            String name = entry.getKey();
            String description = entry.getValue();

            hasValidInput result = getHasValidInput(name, description);
            if (result == null) continue;

            categoryDTOList.add(new CategoryDTO(-1, result.name(), result.description(), isActive, null, null));
        }
        return categoryDTOList;
    }

    private hasValidInput getHasValidInput(String name, String description) throws ErrorHandling.InvalidInputException {
        if (!CategoryValidation.hasValidInput(name, description)) return null;

        name = InputValidator.stringSanitize(name);
        description = InputValidator.stringSanitize(description);
        return new hasValidInput(name, description);
    }

    private record hasValidInput(String name, String description) {
    }
}
