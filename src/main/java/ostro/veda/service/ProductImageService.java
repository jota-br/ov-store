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
                                       boolean isMain, ProcessDataType dmlType) {
        try {
            if (dmlType == null) {
                return null;
            }
            String urlCheck = InputValidator.imageUrlCheck(url);

            if (urlCheck == null) {
                return null;
            }

            return performDmlAction(entityAndId, url, isMain, dmlType);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ProductImageDTO performDmlAction(Map<EntityType, Integer> entityAndId, String url,
                                             boolean isMain, ProcessDataType dmlType) {
        switch (dmlType) {
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
