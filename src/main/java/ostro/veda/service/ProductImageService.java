package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.jpa.ProductImageRepository;

import java.util.Map;

public class ProductImageService {

    public static ProductImageDTO processData(Map<EntityType, Integer> entityAndId, String url, boolean isMain, ProcessDataType dmlType) {

        String urlCheck = InputValidator.imageUrlCheck(url);

        if (urlCheck == null) {
            return null;
        }

        switch (dmlType) {
            case ADD -> {
                return ProductImageRepository.addImage(url, isMain);
            }
            case UPDATE -> {
                int id = entityAndId.getOrDefault(EntityType.PRODUCT_IMAGE, -1);
                return ProductImageRepository.updateImage(id, url, isMain);
            }
        }
        return null;
    }
}
