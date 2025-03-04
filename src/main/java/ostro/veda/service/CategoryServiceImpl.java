package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.db.helpers.database.Action;
import ostro.veda.service.events.AuditEvent;

@Slf4j
@Component
public class CategoryServiceImpl implements CategoryService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final CategoryRepository categoryRepositoryImpl;

    @Autowired
    public CategoryServiceImpl(ApplicationEventPublisher applicationEventPublisher, CategoryRepository categoryRepositoryImpl) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.categoryRepositoryImpl = categoryRepositoryImpl;
    }

    @Override
    public CategoryDTO add(CategoryDTO categoryDTO) {
        log.info("add() new Category = {}", categoryDTO.getName());
        try {
            ValidateUtil.validateCategory(categoryDTO);
            categoryDTO = SanitizeUtil.sanitizeCategory(categoryDTO);

            categoryDTO = categoryRepositoryImpl.add(categoryDTO);

            AuditEvent event = AuditEvent.builder()
                    .source(this)
                    .action(Action.INSERT)
                    .categoryDTO(categoryDTO)
                    .userId(1)
                    .id(categoryDTO.getCategoryId())
                    .build();
            applicationEventPublisher.publishEvent(event);

            return categoryDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        log.info("update() Category = {}", categoryDTO.getCategoryId());
        try {
            ValidateUtil.validateCategory(categoryDTO);
            categoryDTO = SanitizeUtil.sanitizeCategory(categoryDTO);

            categoryDTO = categoryRepositoryImpl.update(categoryDTO);

            AuditEvent event = AuditEvent.builder()
                    .source(this)
                    .action(Action.UPDATE)
                    .categoryDTO(categoryDTO)
                    .userId(1)
                    .id(categoryDTO.getCategoryId())
                    .build();
            applicationEventPublisher.publishEvent(event);

            return categoryDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
