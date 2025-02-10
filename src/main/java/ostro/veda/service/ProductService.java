package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.ProductRepository;

import java.util.List;
import java.util.Map;

public class ProductService {

    public static ProductDTO processData(Map<EntityType, Integer> entityAndId, String name, String description, double price, int stock, boolean isActive,
                                         List<CategoryDTO> categories, List<ProductImageDTO> images, ProcessDataType dmlType) {

        int nameMinLength = 5;

        String nameCheck = InputValidator.stringChecker(name, true, true, nameMinLength);
        String descriptionCheck = InputValidator.stringChecker(description, true, true,-1);

        if (nameCheck == null || descriptionCheck == null || price < 0.0 || stock < 0) {
            return null;
        }

        switch (dmlType) {
            case ADD -> {
                return ProductRepository.addProduct(name, description, price, stock, isActive, categories, images);
            }
            case UPDATE -> {
                return ProductRepository.updateProduct(entityAndId, name, description, price, stock, isActive, categories, images);
            }
        }
        return null;
    }
}
