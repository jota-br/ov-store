package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.columns.CategoryColumns;
import ostro.veda.db.jpa.Category;

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
    public CategoryDTO add(CategoryDTO categoryDTO) {
        log.info("add() Category = {}", categoryDTO.getName());

        try {

            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            boolean isActive = categoryDTO.isActive();

            List<Category> result = this.entityManagerHelper.findByFields(this.entityManager, Category.class,
                    Map.of(CategoryColumns.NAME.getColumnName(), name));

            if (result != null && !result.isEmpty()) return null;
            Category category = new Category(0, name, description, isActive, null, null, 0);

            this.entityManager.persist(category);
            return category.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public CategoryDTO update(CategoryDTO categoryDTO) {
        log.info("update() Category = {}", categoryDTO.getCategoryId());

        try {

            int categoryId = categoryDTO.getCategoryId();
            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            boolean active = categoryDTO.isActive();

            Category category = this.entityManager.find(Category.class, categoryId);
            if (category == null) return null;

            Category categoryNewData = new Category()
                    .setCategoryId(categoryDTO.getCategoryId())
                    .setName(name)
                    .setDescription(description)
                    .setActive(active);

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
