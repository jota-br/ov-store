package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.util.Action;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.CategoryRepository;

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

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, categoryDTO, 1);

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

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, categoryDTO, 1);

            return categoryDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
