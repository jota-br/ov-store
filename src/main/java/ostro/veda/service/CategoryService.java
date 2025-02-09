package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.CategoryRepository;

public class CategoryService {

    public static CategoryDTO processData(String name, String description, boolean isActive) {

        int nameMinLength = 5;

        String nameCheck = InputValidator.stringChecker(name, true, true, nameMinLength);
        String descriptionCheck = InputValidator.stringChecker(description, true, true,-1);

        if (nameCheck == null || descriptionCheck == null) {
            return null;
        }

        return CategoryRepository.addCategory(name, description, isActive);
    }
}
