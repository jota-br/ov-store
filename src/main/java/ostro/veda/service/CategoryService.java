package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.CategoryRepository;
import ostro.veda.loggerService.Logger;

import java.util.Map;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO processData(Map<EntityType, Integer> entityAndId, String name, String description,
                                   boolean isActive, ProcessDataType processDataType) {

        try {
            if (!hasValidInput(name, description, processDataType)) return null;
            name = InputValidator.stringSanitize(name);
            description = InputValidator.stringSanitize(description);
//            if (!hasValidLength(name, description)) return null;

            return performDmlAction(entityAndId, name, description, isActive, processDataType);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private boolean hasValidInput(String name, String description, ProcessDataType processDataType)
            throws ErrorHandling.InvalidNameException, ErrorHandling.InvalidDescriptionException {
        return InputValidator.hasValidName(name) &&
                InputValidator.hasValidDescription(description) &&
                processDataType != null;
    }

    private boolean hasValidLength(String name, String description) throws ErrorHandling.InvalidLengthException {
        int emptyMin = 0;
        int minimumLength = 1;
        int nameMaxLength = 255;
        int descriptionMaxLength = 510;

        return InputValidator.hasValidLength(name, minimumLength, nameMaxLength) &&
                InputValidator.hasValidLength(description, emptyMin, descriptionMaxLength);
    }

    private CategoryDTO performDmlAction(Map<EntityType, Integer> entityAndId, String name, String description,
                                         boolean isActive, ProcessDataType processDataType) {
        switch (processDataType) {
            case ADD -> {
                return this.categoryRepository.addCategory(name, description, isActive);
            }
            case UPDATE -> {
                int id = entityAndId.getOrDefault(EntityType.CATEGORY, -1);
                return this.categoryRepository.updateCategory(id, name, description, isActive);
            }
            default -> {
                return null;
            }
        }
    }
}
