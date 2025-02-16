package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.ProductRepository;

import java.util.ArrayList;
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

    public ProductDTO processData(String nameProduct, String descriptionProduct, double priceProduct, int stockProduct, boolean isActiveProduct,
                                  List<String> categories, Map<String, Boolean> images, ProcessDataType processDataType,
                                  Map<EntityType, Integer> entityAndId) {
//        Map<String, Boolean> images = k:Url v:isMain

        try {
            if (!hasValidInput(nameProduct, descriptionProduct, priceProduct, stockProduct, processDataType)) return null;
            nameProduct = InputValidator.stringSanitize(nameProduct);
            descriptionProduct = InputValidator.stringSanitize(descriptionProduct);
//            if (!hasValidLength(nameProduct, descriptionProduct)) return null;

            List<CategoryDTO> categoriesList = getCategoryDTOList(isActiveProduct, categories, processDataType, entityAndId);
            List<ProductImageDTO> imagesList = getImageDTOList(images, processDataType, entityAndId);

            return performDmlAction(entityAndId, nameProduct, descriptionProduct, priceProduct, stockProduct, isActiveProduct,
                    categoriesList, imagesList, processDataType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<ProductImageDTO> getImageDTOList(Map<String, Boolean> images, ProcessDataType processDataType, Map<EntityType, Integer> entityAndId) {
        List<ProductImageDTO> imagesList = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : images.entrySet()) {
            String url = entry.getKey();
            boolean isMain = entry.getValue();
            ProductImageDTO productImageDTO = this.productImageService.processData(entityAndId, url, isMain, processDataType);
            imagesList.add(productImageDTO);
        }
        return imagesList;
    }

    private List<CategoryDTO> getCategoryDTOList(boolean isActiveProduct, List<String> categories, ProcessDataType processDataType, Map<EntityType, Integer> entityAndId) {
        List<CategoryDTO> categoriesList = new ArrayList<>();
        for (String string : categories) {
            CategoryDTO category = this.categoryService.processData(entityAndId, string, "", isActiveProduct, processDataType);
            categoriesList.add(category);
        }
        return categoriesList;
    }

    private boolean hasValidInput(String nameProduct, String descriptionProduct, double priceProduct, int stockProduct,
                                  ProcessDataType processDataType) throws ErrorHandling.InvalidNameException, ErrorHandling.InvalidDescriptionException {
        return InputValidator.hasValidName(nameProduct) &&
                InputValidator.hasValidDescription(descriptionProduct) &&
                priceProduct >= 0.0 &&
                stockProduct >= 0 &&
                processDataType != null;
    }

    private boolean hasValidLength(String nameProduct, String descriptionProduct) throws ErrorHandling.InvalidLengthException {
        int emptyMin = 0;
        int standardMin = 1;
        int standardMax = 255;
        int descriptionMax = 510;
        return InputValidator.hasValidLength(nameProduct, standardMin, standardMax) &&
                InputValidator.hasValidLength(descriptionProduct, emptyMin, descriptionMax);
    }

    private ProductDTO performDmlAction(Map<EntityType, Integer> entityAndId, String name, String description,
                                        double price, int stock, boolean isActive, List<CategoryDTO> categories,
                                        List<ProductImageDTO> images, ProcessDataType processDataType) {
        switch (processDataType) {
            case ADD -> {
                return this.productRepository.addProduct(name, description, price, stock, isActive, categories, images);
            }
            case UPDATE -> {
                return this.productRepository.updateProduct(entityAndId, name, description, price, stock, isActive, categories, images);
            }
            default -> {
                return null;
            }
        }
    }
}
