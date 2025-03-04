package main.java.ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.ProductDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.common.validation.SanitizeUtil;
import main.java.ostro.veda.common.validation.ValidateUtil;
import main.java.ostro.veda.db.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepositoryImpl;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepositoryImpl) {
        this.productRepositoryImpl = productRepositoryImpl;
    }

    @Override
    public ProductDTO add(@NonNull ProductDTO product) {
        try {
            ValidateUtil.validateProduct(product);
            ProductDTO productDTO = SanitizeUtil.sanitizeProduct(product);
            return this.productRepositoryImpl.add(productDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public ProductDTO update(@NonNull ProductDTO product) {
        try {
            ValidateUtil.validateProduct(product);
            ProductDTO productDTO = SanitizeUtil.sanitizeProduct(product);
            return this.productRepositoryImpl.update(productDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
