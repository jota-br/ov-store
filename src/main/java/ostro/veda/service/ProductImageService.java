package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.ProductImageRepository;

import java.util.Map;

public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public ProductImageDTO processData(Map<EntityType, Integer> entityAndId, String url,
                                       boolean isMain, ProcessDataType processDataType) {
        try {
            if (!hasValidInput(url, processDataType)) return null;
            url = InputValidator.encodeUrl(url);
            if (!hasValidLength(url)) return null;

            return performDmlAction(entityAndId, url, isMain, processDataType);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean hasValidInput(String url, ProcessDataType processDataType) {
        return processDataType != null && InputValidator.hasValidUrl(url);
    }

    private static boolean hasValidLength(String input) {
        int min = 12;
        int max = 255;
        return InputValidator.hasValidLength(input, min, max);
    }

    private ProductImageDTO performDmlAction(Map<EntityType, Integer> entityAndId, String url,
                                             boolean isMain, ProcessDataType processDataType) {
        switch (processDataType) {
            case ADD -> {
                return this.productImageRepository.addImage(url, isMain);
            }
            case UPDATE -> {
                int id = entityAndId.getOrDefault(EntityType.PRODUCT_IMAGE, -1);
                return this.productImageRepository.updateImage(id, url, isMain);
            }

            default -> {
                return null;
            }
        }
    }
}
