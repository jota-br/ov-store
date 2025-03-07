package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.common.util.Action;
import ostro.veda.service.events.AuditEvent;

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
    public ProductDTO add(@NonNull ProductDTO product) {
        try {
            ValidateUtil.validateProduct(product);
            ProductDTO productDTO = SanitizeUtil.sanitizeProduct(product);

            productDTO = this.productRepositoryImpl.add(productDTO);

            AuditEvent event = AuditEvent.builder()
                    .source(this)
                    .action(Action.INSERT)
                    .productDTO(productDTO)
                    .userId(1)
                    .id(product.getProductId())
                    .build();
            applicationEventPublisher.publishEvent(event);

            return productDTO;

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

            productDTO = this.productRepositoryImpl.update(productDTO);

            AuditEvent event = AuditEvent.builder()
                    .source(this)
                    .action(Action.UPDATE)
                    .productDTO(productDTO)
                    .userId(1)
                    .id(product.getProductId())
                    .build();
            applicationEventPublisher.publishEvent(event);

            return productDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
