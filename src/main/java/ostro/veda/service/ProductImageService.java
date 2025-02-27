package ostro.veda.service;

import ostro.veda.db.ProductImageRepository;

public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    /**
     *
     * @param productImageRepository ProductImageRepository
     */
    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }
}
