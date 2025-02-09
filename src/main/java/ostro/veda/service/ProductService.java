package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.ProductRepository;

import java.util.List;

public class ProductService {

    public static ProductDTO processData(String name, String description, double price, int stock, boolean isActive,
                                         List<CategoryDTO> categories, List<ProductImageDTO> images) {

        int nameMinLength = 5;

        String nameCheck = InputValidator.stringChecker(name, true, true, nameMinLength);
        String descriptionCheck = InputValidator.stringChecker(description, true, true,-1);

        if (nameCheck == null || descriptionCheck == null || price < 0.0 || stock < 0) {
            return null;
        }

        return ProductRepository.addProduct(name, description, price, stock, isActive, categories, images);
    }
}
