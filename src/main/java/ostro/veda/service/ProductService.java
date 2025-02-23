package ostro.veda.service;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.ProductValidation;
import ostro.veda.common.validation.StringSanitize;
import ostro.veda.db.ProductRepository;
import ostro.veda.loggerService.Logger;

import java.util.List;
import java.util.Map;

public class ProductService {

    private final CategoryService categoryService;
    private final ProductImageService productImageService;
    private final ProductRepository productRepository;

    public ProductService(CategoryService categoryService,
                          ProductImageService productImageService, ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.productImageService = productImageService;
        this.productRepository = productRepository;
    }

    public ProductDTO addProduct(String nameProduct, String descriptionProduct, double priceProduct, int stockProduct, boolean isActiveProduct,
                                 Map<String, String> categoryAndDescription, Map<String, Boolean> images) {
        try {
            if (!ProductValidation.hasValidInput(nameProduct, descriptionProduct, priceProduct, stockProduct))
                return null;
            nameProduct = StringSanitize.stringSanitize(nameProduct);
            descriptionProduct = StringSanitize.stringSanitize(descriptionProduct);

            List<CategoryDTO> categoriesList = getCategoryDTOList(categoryAndDescription, isActiveProduct);
            List<ProductImageDTO> imagesList = getImageDTOList(images);

            return this.productRepository.addProduct(nameProduct, descriptionProduct, priceProduct, stockProduct, isActiveProduct, categoriesList, imagesList);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    public ProductDTO updateProduct(int productId, String nameProduct, String descriptionProduct, double priceProduct, int stockProduct, boolean isActiveProduct,
                                    Map<String, String> categoryAndDescription, Map<String, Boolean> images) {
        try {
            if (!ProductValidation.hasValidInput(nameProduct, descriptionProduct, priceProduct, stockProduct))
                return null;
            nameProduct = StringSanitize.stringSanitize(nameProduct);
            descriptionProduct = StringSanitize.stringSanitize(descriptionProduct);

            List<CategoryDTO> categoriesList = getCategoryDTOList(categoryAndDescription, isActiveProduct);
            List<ProductImageDTO> imagesList = getImageDTOList(images);

            return this.productRepository.updateProduct(productId, nameProduct, descriptionProduct, priceProduct, stockProduct, isActiveProduct, categoriesList, imagesList);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private List<ProductImageDTO> getImageDTOList(Map<String, Boolean> images) {
        if (images == null) {
            return null;
        }
        return this.productImageService.addProduct(images);
    }

    private List<CategoryDTO> getCategoryDTOList(Map<String, String> categoryAndDescription, boolean isActiveProduct)
            throws ErrorHandling.InvalidInputException {
        if (categoryAndDescription == null) {
            return null;
        }
        return this.categoryService.addProduct(categoryAndDescription, isActiveProduct);
    }
}
