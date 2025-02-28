package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.CategoryColumns;
import ostro.veda.db.jpa.Category;
import ostro.veda.loggerService.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryRepository extends Repository {

    public CategoryRepository() {
    }

    /**
     * @param em EntityManager
     */
    public CategoryRepository(EntityManager em) {
        super(em);
    }

    /**
     * @param categories List with CategoryDTO.
     * @return List with CategoryDTO.
     */
    public List<CategoryDTO> addCategories(List<CategoryDTO> categories) {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (CategoryDTO entity : categories) {
                CategoryDTO categoryDTO = addCategory(entity);
                if (categoryDTO == null) continue;
                categoryDTOList.add(categoryDTO);
        }

        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            boolean isActive = categoryDTO.isActive();

            List<Category> result = this.entityManagerHelper.findByFields(this.em, Category.class,
                    Map.of(CategoryColumns.NAME.getColumnName(), name));

            if (result != null && !result.isEmpty()) return null;
            Category category = new Category(name, description, isActive);

            this.em.persist(category);

            transaction.commit();

            return category.transformToDto();
        } catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
            return null;
        }
    }

    /**
     * @param categories List with CategoryDTO.
     * @return List with CategoryDTO.
     */
    public List<CategoryDTO> updateCategories(List<CategoryDTO> categories) {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (CategoryDTO entity : categories) {
            CategoryDTO categoryDTO = updateCategory(entity);
            if (categoryDTO == null) continue;
            categoryDTOList.add(categoryDTO);
        }

        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            int categoryId = categoryDTO.getCategoryId();
            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            boolean active = categoryDTO.isActive();

            Category category = this.em.find(Category.class, categoryId);
            if (category == null) return null;

            category.updateCategory(new Category(name, description, active));
            this.em.merge(category);

            transaction.commit();

            return category.transformToDto();
        } catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
            return null;
        }
    }
}
