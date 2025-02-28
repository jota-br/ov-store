package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.db.ProductImageRepository;

@Slf4j
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }
}
