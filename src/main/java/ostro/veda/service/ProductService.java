package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.ProductRepository;

@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO addProduct(ProductDTO product) {
        try {
            ValidateUtil.validateProduct(product);
            ProductDTO productDTO = SanitizeUtil.sanitizeProduct(product);
            return this.productRepository.addProduct(productDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public ProductDTO updateProduct(ProductDTO product) {
        try {
            ValidateUtil.validateProduct(product);
            ProductDTO productDTO = SanitizeUtil.sanitizeProduct(product);
            return this.productRepository.updateProduct(productDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
