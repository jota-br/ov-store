package ostro.veda.service;

import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.common.validation.ProductValidation;
import ostro.veda.common.validation.UrlEncoder;
import ostro.veda.db.ProductImageRepository;
import ostro.veda.loggerService.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public List<ProductImageDTO> addProduct(Map<String, Boolean> images) {
        try {
            List<ProductImageDTO> productImageDTOList = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : images.entrySet()) {
                String url = entry.getKey();
                boolean isMain = entry.getValue();
                if (!ProductValidation.hasValidInput(url)) continue;
                url = UrlEncoder.encodeUrl(url);
                productImageDTOList.add(new ProductImageDTO(-1, url, isMain));
            }
            return productImageDTOList;
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
