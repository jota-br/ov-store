package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.CategoryRepository;

import java.util.Map;

public class CategoryService {

    public static CategoryDTO processData(Map<EntityType, Integer> entityAndId, String name, String description, boolean isActive, ProcessDataType dmlType) {

        int nameMinLength = 5;

        String nameCheck = InputValidator.stringChecker(name, true, true, nameMinLength);
        String descriptionCheck = InputValidator.stringChecker(description, true, true,-1);

        if (nameCheck == null || descriptionCheck == null) {
            return null;
        }

        switch(dmlType) {
            case ADD -> {
                return CategoryRepository.addCategory(name, description, isActive);
            }
            case UPDATE -> {
                int id = entityAndId.getOrDefault(EntityType.CATEGORY, -1);
                return CategoryRepository.updateCategory(id, name, description, isActive);
            }
        }
        return null;
    }
}
