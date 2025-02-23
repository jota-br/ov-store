package ostro.veda.service;

import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.common.validation.ProductValidation;
import ostro.veda.common.validation.UrlEncoder;
import ostro.veda.db.ProductImageRepository;
import ostro.veda.loggerService.Logger;

import java.util.ArrayList;
import java.util.List;

public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    /**
     * Validates Image data to be persisted with Product. Called by ProductService.
     * @param images
     * @return List<ProductImageDTO>
     */
    public List<ProductImageDTO> addProduct(List<ProductImageDTO> images) {
        try {
            List<ProductImageDTO> productImageDTOList = new ArrayList<>();
            for (ProductImageDTO entity : images) {
                String url = entity.getImageUrl();
                boolean isMain = entity.isMain();
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
