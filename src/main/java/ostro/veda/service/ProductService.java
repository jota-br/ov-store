package ostro.veda.service;

import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
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
            ValidateUtil.validateProduct(product);
            ProductDTO productDTO = SanitizeUtil.sanitizeProduct(product);
            return this.productRepository.addProduct(productDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            Logger.log(e);
            return null;
        }
    }

    public ProductDTO updateProduct(ProductDTO product) {
        try {
            ValidateUtil.validateProduct(product);
            ProductDTO productDTO = SanitizeUtil.sanitizeProduct(product);
            return this.productRepository.updateProduct(productDTO);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
