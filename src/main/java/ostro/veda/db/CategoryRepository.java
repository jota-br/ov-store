package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.helpers.columns.CategoryColumns;
import ostro.veda.db.jpa.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryRepository extends Repository {

    public CategoryRepository() {
    }

    /**
     *
     * @param em EntityManager
     */
    public CategoryRepository(EntityManager em) {
        super(em);
    }

    /**
     *
     * @param categories List with CategoryDTO.
     * @return List with CategoryDTO.
     */
    public List<CategoryDTO> addCategory(List<CategoryDTO> categories) {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (CategoryDTO entity : categories) {
            String name = entity.getName();
            String description = entity.getDescription();
            boolean isActive = entity.isActive();

            List<Category> result = this.entityManagerHelper.findByFields(this.em, Category.class,
                    Map.of(CategoryColumns.NAME.getColumnName(), name));
            Category category = null;
            if (result != null && !result.isEmpty()) {
                // If the Category name is already in use it will not be persisted to the DB.
                // Next in the List - if any - will be checked.
                continue;
            } else {
                category = new Category(name, description, isActive);
                categoryDTOList.add(category.transformToDto());
            }

            boolean isInserted = this.entityManagerHelper.executePersist(this.em, category);
            if (!isInserted) {
                return null;
            }
        }

        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }

    /**
     *
     * @param categories List with CategoryDTO.
     * @return List with CategoryDTO.
     */
    public List<CategoryDTO> updateCategory(List<CategoryDTO> categories) {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (CategoryDTO entity : categories) {
            int categoryId = entity.getCategoryId();
            String name = entity.getName();
            String description = entity.getDescription();
            boolean active = entity.isActive();

            Category category = this.em.find(Category.class, categoryId);
            if (category == null) continue;

            category.updateCategory(new Category(name, description, active));
            boolean isInserted = this.entityManagerHelper.executeMerge(this.em, category);

            if (isInserted) {
                categoryDTOList.add(category.transformToDto());
            }
        }

        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }
}
