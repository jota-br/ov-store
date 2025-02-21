package ostro.veda.db;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.helpers.columns.CategoryColumns;
import ostro.veda.db.jpa.Category;

import java.util.List;
import java.util.Map;

public class CategoryRepository extends Repository {

    public CategoryDTO addCategory(String name, String description, boolean isActive) {

        List<Category> result = this.entityManagerHelper.findByFields(this.em, Category.class,
                Map.of(CategoryColumns.NAME.getColumnName(), name));
        Category category = null;
        if (result != null && !result.isEmpty()) {
            category = result.get(0);
        } else {
            category = new Category(name, description, isActive);
        }

        boolean isInserted = this.entityManagerHelper.executePersist(this.em, category);
        if (!isInserted) {
            return null;
        }

        return category.transformToDto();
    }

    public CategoryDTO updateCategory(int categoryId, String name, String description, boolean isActive) {

        Category category = this.em.find(Category.class, categoryId);

        if (category == null) {
            return addCategory(name, description, isActive);
        }

        category.updateCategory(new Category(name, description, isActive));

        boolean isInserted = this.entityManagerHelper.executeMerge(this.em, category);
        if (!isInserted) {
            return null;
        }

        return category.transformToDto();
    }
}
