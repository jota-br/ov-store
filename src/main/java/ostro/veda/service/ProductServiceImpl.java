package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.ProductDto;
import ostro.veda.repository.interfaces.ProductRepository;
import ostro.veda.service.interfaces.ProductService;
import ostro.veda.util.enums.Action;
import ostro.veda.util.sanitization.SanitizeProduct;

@Slf4j
@Component
public class ProductServiceImpl implements ProductService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProductRepository productRepositoryImpl;

    @Autowired
    public ProductServiceImpl(ApplicationEventPublisher applicationEventPublisher, ProductRepository productRepositoryImpl) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.productRepositoryImpl = productRepositoryImpl;
    }

    @Override
    public ProductDto add(ProductDto product) {

        log.info("add() new Product");

        try {

            ProductDto productDTO = new SanitizeProduct().sanitize(product);

            productDTO = this.productRepositoryImpl.add(productDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, productDTO, 1);

            return productDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public ProductDto update(ProductDto product) {

        log.info("update() Product = {}", product.getProductId());

        try {

            ProductDto productDTO = new SanitizeProduct().sanitize(product);

            productDTO = this.productRepositoryImpl.update(productDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, productDTO, 1);

            return productDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }
}
