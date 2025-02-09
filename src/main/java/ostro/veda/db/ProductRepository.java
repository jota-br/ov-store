package ostro.veda.db;

import org.hibernate.Session;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.helpers.SessionDml;
import ostro.veda.db.jpa.Category;
import ostro.veda.db.jpa.Product;
import ostro.veda.db.jpa.ProductImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    public static ProductDTO addProduct(String name, String description, double price, int stock, boolean isActive,
                                        List<CategoryDTO> categories, List<ProductImageDTO> images) {

        Session session = DbConnection.getOpenSession();
        List<Product> result = SessionDml.findByFields(session, Product.class, Map.of("name", name));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        List<Category> categoriesList = new ArrayList<>();
        for (CategoryDTO c : categories) {
            Category category = session.find(Category.class, c.getCategoryId());
            categoriesList.add(category);
        }

        List<ProductImage> imagesList = new ArrayList<>();
        for (ProductImageDTO pi : images) {
            ProductImage productImage = null;
            if (pi.getProductImageId() > 0) {
                productImage = session.find(ProductImage.class, pi.getProductImageId());
            }
            if (productImage == null) {
                productImage = new ProductImage(pi.getImageUrl(), pi.isMain());
            }
            imagesList.add(productImage);
        }

        Product product = new Product(name, description, price, stock, isActive, categoriesList, imagesList);
        boolean isInserted = SessionDml.executePersist(session, product);
        if (!isInserted) {
            return null;
        }

        ProductDTO dto = product.transformToDto();
        DbConnection.closeSession(session);

        return dto;
    }
}
