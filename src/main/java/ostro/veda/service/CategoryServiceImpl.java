package main.java.ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.CategoryDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.common.validation.SanitizeUtil;
import main.java.ostro.veda.common.validation.ValidateUtil;
import main.java.ostro.veda.db.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepositoryImpl;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepositoryImpl) {
        this.categoryRepositoryImpl = categoryRepositoryImpl;
    }

    @Override
    public CategoryDTO add(CategoryDTO categoryDTO) {
        log.info("add() new Category = {}", categoryDTO.getName());
        try {
            ValidateUtil.validateCategory(categoryDTO);
            categoryDTO = SanitizeUtil.sanitizeCategory(categoryDTO);
            return categoryRepositoryImpl.add(categoryDTO);
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
            return categoryRepositoryImpl.update(categoryDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
