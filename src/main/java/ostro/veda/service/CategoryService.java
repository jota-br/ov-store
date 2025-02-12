package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.CategoryRepository;

import java.util.Map;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO processData(Map<EntityType, Integer> entityAndId, String name, String description,
                                   boolean isActive, ProcessDataType dmlType) {

        try {
            if (dmlType == null) {
                return null;
            }

            int nameMinLength = 5;

            String nameCheck = InputValidator.stringChecker(name, true, true, false, nameMinLength);
            String descriptionCheck = InputValidator.stringChecker(description, true, true, true, -1);

            if (nameCheck == null || descriptionCheck == null) {
                return null;
            }

            return performDmlAction(entityAndId, name, description, isActive, dmlType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CategoryDTO performDmlAction(Map<EntityType, Integer> entityAndId, String name, String description,
                                        boolean isActive, ProcessDataType dmlType) {
        switch(dmlType) {
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
