package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.CategoryDto;
import ostro.veda.repository.interfaces.CategoryRepository;
import ostro.veda.service.interfaces.CategoryService;
import ostro.veda.util.enums.Action;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.validation.SanitizeUtil;
import ostro.veda.util.validation.ValidatorUtil;

@Slf4j
@Component
public class CategoryServiceImpl implements CategoryService {

    private final ValidatorUtil validatorUtil;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CategoryRepository categoryRepositoryImpl;

    @Autowired
    public CategoryServiceImpl(ValidatorUtil validatorUtil, ApplicationEventPublisher applicationEventPublisher, CategoryRepository categoryRepositoryImpl) {
        this.validatorUtil = validatorUtil;
        this.applicationEventPublisher = applicationEventPublisher;
        this.categoryRepositoryImpl = categoryRepositoryImpl;
    }

    @Override
    public CategoryDto add(CategoryDto categoryDTO) {

        log.info("add() new Category = {}", categoryDTO.getName());

        try {

            validatorUtil.validate(categoryDTO);
            categoryDTO = SanitizeUtil.sanitizeCategory(categoryDTO);

            categoryDTO = categoryRepositoryImpl.add(categoryDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, categoryDTO, 1);

            return categoryDTO;

        } catch (InputException.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public CategoryDto update(CategoryDto categoryDTO) {

        log.info("update() Category = {}", categoryDTO.getCategoryId());

        try {

            validatorUtil.validate(categoryDTO);
            categoryDTO = SanitizeUtil.sanitizeCategory(categoryDTO);

            categoryDTO = categoryRepositoryImpl.update(categoryDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, categoryDTO, 1);

            return categoryDTO;

        } catch (InputException.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }
}
