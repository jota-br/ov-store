package ostro.veda.db;

import org.hibernate.Session;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.db.helpers.SessionDml;
import ostro.veda.db.jpa.Category;

import java.util.List;
import java.util.Map;

public class CategoryRepository {

    public static CategoryDTO addCategory(String name, String description, boolean isActive) {

        Session session = DbConnection.getOpenSession();
        List<Category> result = SessionDml.findByFields(session, Category.class, Map.of("name", name));
        Category category = null;
        if (result != null && !result.isEmpty()) {
            category = result.get(0);
        } else {
            category = new Category(name, description, isActive);
        }

        boolean isInserted = SessionDml.executePersist(session, category);
        if (!isInserted) {
            return null;
        }

        CategoryDTO dto = category.transformToDto();
        DbConnection.closeSession(session);

        return dto;
    }
}
