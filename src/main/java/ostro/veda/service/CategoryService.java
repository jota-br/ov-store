package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.dto.CategoryDTO;
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

    public List<CategoryDTO> addProduct(Map<String, String> categoryAndDescription, boolean isActive) {

        try {
            List<CategoryDTO> categoryDTOList = new ArrayList<>();
            for (Map.Entry<String, String> entry : categoryAndDescription.entrySet()) {
                String name = entry.getKey();
                String description = entry.getValue();
                if (!CategoryValidation.hasValidInput(name, description)) continue;
                name = InputValidator.stringSanitize(name);
                description = InputValidator.stringSanitize(description);
                categoryDTOList.add(new CategoryDTO(-1, name, description, isActive, null, null));
            }

            return categoryDTOList.isEmpty() ? null : categoryDTOList;
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
