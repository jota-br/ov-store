package ostro.veda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import ostro.veda.repository.interfaces.CategoryRepository;
import ostro.veda.model.dto.CategoryDto;
import ostro.veda.repository.helpers.EntityManagerHelper;
import ostro.veda.repository.helpers.enums.CategoryColumns;
import ostro.veda.repository.dao.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CategoryRepositoryImpl implements CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public CategoryRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    @Transactional
    public CategoryDto add(CategoryDto categoryDTO) {
        log.info("add() Category = {}", categoryDTO.getName());

        try {

            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            boolean isActive = categoryDTO.isActive();

            List<Category> result = this.entityManagerHelper.findByFields(this.entityManager, Category.class,
                    Map.of(CategoryColumns.NAME.getColumnName(), name));

            if (result != null && !result.isEmpty()) return null;
            Category category = Category
                    .builder()
                    .categoryId(0)
                    .name(name)
                    .description(description)
                    .isActive(isActive)
                    .build();

            this.entityManager.persist(category);
            return category.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDTO) {
        log.info("update() Category = {}", categoryDTO.getCategoryId());

        try {

            int categoryId = categoryDTO.getCategoryId();
            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            boolean active = categoryDTO.isActive();

            Category category = this.entityManager.find(Category.class, categoryId);
            if (category == null) return null;

            Category categoryNewData = Category
                    .builder()
                    .categoryId(categoryDTO.getCategoryId())
                    .name(name)
                    .description(description)
                    .isActive(active)
                    .build();

            category.updateCategory(categoryNewData);
            this.entityManager.merge(category);
            return category.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
