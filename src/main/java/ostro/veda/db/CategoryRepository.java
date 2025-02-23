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

    public CategoryRepository(EntityManager em) {
        super(em);
    }

    /**
     * Persist new Category if the category name is unique.
     * @param categoryAndDescription Contains Category name and description.
     * @param isActive True if the Category is active or false otherwise.
     * @return Returns List containing all CategoryDTO which were persisted, or null if none.
     */
    public List<CategoryDTO> addCategory(Map<String, String> categoryAndDescription, List<Boolean> isActive) {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, String> entry : categoryAndDescription.entrySet()) {
            String name = entry.getKey();
            String description = entry.getValue();

            List<Category> result = this.entityManagerHelper.findByFields(this.em, Category.class,
                    Map.of(CategoryColumns.NAME.getColumnName(), name));
            Category category = null;
            if (result != null && !result.isEmpty()) {
                // If the Category name is already in use it will not be persisted to the DB.
                // Next in the List - if any - will be checked.
                i++;
                continue;
            } else {
                category = new Category(name, description, isActive.get(i));
            }

            boolean isInserted = this.entityManagerHelper.executePersist(this.em, category);
            if (!isInserted) {
                return null;
            }
            categoryDTOList.add(category.transformToDto());
            i++;
        }

        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }

    public List<CategoryDTO> updateProduct(Map<Integer, List<Map<String, String>>> idNameAndDescription, List<Boolean> isActive) {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        int i = 0;
        for (Map.Entry<Integer, List<Map<String, String>>> entrySet : idNameAndDescription.entrySet()) {
            int categoryId = entrySet.getKey();
            for (Map<String, String> e : entrySet.getValue()) {
                for (Map.Entry<String, String> entry : e.entrySet()) {

                    String name = entry.getKey();
                    String description = entry.getValue();
                    boolean active = isActive.get(i);

                    Category category = this.em.find(Category.class, categoryId);
                    if (category == null) continue;

                    category.updateCategory(new Category(name, description, active));
                    boolean isInserted = this.entityManagerHelper.executeMerge(this.em, category);

                    if (isInserted) {
                        categoryDTOList.add(category.transformToDto());
                    }
                }
            }
            i++;
        }

        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }

    /**
     * Update existing categories with new data.
     * @param idNameAndDescription Contains Map with id (key) and Value List<Map<k, v>>,
     *                             the second Map contains name and description for the Category
     *                             id that will be updated.
     * @param isActive defines if the category is active.
     * @return Returns List containing all CategoryDTO which were persisted, or null if none.
     */
    public List<CategoryDTO> updateCategory(Map<Integer, List<Map<String, String>>> idNameAndDescription, List<Boolean> isActive) {

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        int i = 0;
        for (Map.Entry<Integer, List<Map<String, String>>> entrySet : idNameAndDescription.entrySet()) {
            int categoryId = entrySet.getKey();
            for (Map<String, String> e : entrySet.getValue()) {
                for (Map.Entry<String, String> entry : e.entrySet()) {

                    String name = entry.getKey();
                    String description = entry.getValue();
                    boolean active = isActive.get(i);

                    Category category = this.em.find(Category.class, categoryId);
                    if (category == null) continue;

                    category.updateCategory(new Category(name, description, active));
                    boolean isInserted = this.entityManagerHelper.executeMerge(this.em, category);

                    if (isInserted) {
                        categoryDTOList.add(category.transformToDto());
                    }
                }
            }
            i++;
        }

        return categoryDTOList.isEmpty() ? null : categoryDTOList;
    }
}
