package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
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
                                  List<String> categories, Map<String, Boolean> images, ProcessDataType dmlType,
                                  Map<EntityType, Integer> entityAndId) {
//        Map<String, Boolean> images = k:Url v:isMain

        try {
            if (dmlType == null) {
                return null;
            }

            List<CategoryDTO> categoriesList = new ArrayList<>();
            for (String string : categories) {
                CategoryDTO category = this.categoryService.processData(entityAndId, string, "", isActiveProduct, dmlType);
                categoriesList.add(category);
            }

            List<ProductImageDTO> imagesList = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : images.entrySet()) {
                String url = entry.getKey();
                boolean isMain = entry.getValue();
                ProductImageDTO productImageDTO = this.productImageService.processData(entityAndId, url, isMain, dmlType);
                imagesList.add(productImageDTO);
            }

            int nameMinLength = 5;

            String nameCheck = InputValidator.stringChecker(nameProduct, true, true, nameMinLength);
            String descriptionCheck = InputValidator.stringChecker(descriptionProduct, true, true, -1);

            if (nameCheck == null || descriptionCheck == null || priceProduct < 0.0 || stockProduct < 0) {
                return null;
            }

            return performDmlAction(entityAndId, nameProduct, descriptionProduct, priceProduct, stockProduct, isActiveProduct,
                    categoriesList, imagesList, dmlType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            productRepository.closeEm();
        }
    }

    private ProductDTO performDmlAction(Map<EntityType, Integer> entityAndId, String name, String description,
                                        double price, int stock, boolean isActive, List<CategoryDTO> categories,
                                        List<ProductImageDTO> images, ProcessDataType dmlType) {
        switch (dmlType) {
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
