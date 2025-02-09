package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.jpa.ProductImageRepository;

public class ProductImageService {

    public static ProductImageDTO processData(String url, boolean isMain) {

        String urlCheck = InputValidator.imageUrlCheck(url);

        if (urlCheck == null) {
            return null;
        }

        return ProductImageRepository.addImage(url, isMain);
    }
}
