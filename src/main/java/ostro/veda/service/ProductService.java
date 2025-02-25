package ostro.veda.service;

import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.ProductValidation;
import ostro.veda.db.ProductRepository;
import ostro.veda.loggerService.Logger;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Add new Product.
     * @param product ProductDTO
     * @return ProductDTO
     */
    public ProductDTO addProduct(ProductDTO product) {
        try {
            return this.productRepository.addProduct(ProductValidation.validateAndSanitizeProduct(product));
        } catch (ErrorHandling.InvalidInputException e) {
            Logger.log(e);
            return null;
        }
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        try {
            return this.productRepository.updateProduct(ProductValidation.validateAndSanitizeProduct(productDTO));
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
