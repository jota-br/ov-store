package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.ProductDto;
import ostro.veda.repository.interfaces.ProductRepository;
import ostro.veda.service.interfaces.ProductService;
import ostro.veda.util.enums.Action;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.validation.SanitizeUtil;
import ostro.veda.util.validation.ValidatorUtil;

@Slf4j
@Component
public class ProductServiceImpl implements ProductService {

    private final ValidatorUtil validatorUtil;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProductRepository productRepositoryImpl;

    @Autowired
    public ProductServiceImpl(ValidatorUtil validatorUtil, ApplicationEventPublisher applicationEventPublisher, ProductRepository productRepositoryImpl) {
        this.validatorUtil = validatorUtil;
        this.applicationEventPublisher = applicationEventPublisher;
        this.productRepositoryImpl = productRepositoryImpl;
    }

    @Override
    public ProductDto add(@NonNull ProductDto product) {

        log.info("add() new Product");

        try {

            validatorUtil.validate(product);
            ProductDto productDTO = SanitizeUtil.sanitizeProduct(product);

            productDTO = this.productRepositoryImpl.add(productDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, productDTO, 1);

            return productDTO;

        } catch (InputException.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public ProductDto update(@NonNull ProductDto product) {

        log.info("update() Product = {}", product.getProductId());

        try {

            validatorUtil.validate(product);

            ProductDto productDTO = SanitizeUtil.sanitizeProduct(product);

            productDTO = this.productRepositoryImpl.update(productDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, productDTO, 1);

            return productDTO;

        } catch (InputException.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }
}
