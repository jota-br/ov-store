package ostro.veda.db;

import jakarta.persistence.OptimisticLockException;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.columns.ProductColumns;
import ostro.veda.db.jpa.Category;
import ostro.veda.db.jpa.Product;
import ostro.veda.db.jpa.ProductImage;
import ostro.veda.service.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductRepository extends Repository {

    public ProductRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }


    public ProductDTO addProduct(String name, String description, double price, int stock, boolean isActive,
                                 List<CategoryDTO> categories, List<ProductImageDTO> images) {

        List<Product> result = this.entityManagerHelper.findByFields(this.em, Product.class,
                Map.of(ProductColumns.NAME.getColumnName(), name));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        List<Category> categoriesList = getCategoriesList(categories);
        List<ProductImage> imagesList = getImagesList(images);

        Product product = new Product(name, description, price, stock, isActive, categoriesList, imagesList);
        boolean isInserted = this.entityManagerHelper.executePersist(this.em, product);
        if (!isInserted) {
            return null;
        }

        return product.transformToDto();
    }

    public ProductDTO updateProduct(Map<EntityType, Integer> entityAndId, String name, String description, double price, int stock, boolean isActive,
                                           List<CategoryDTO> categories, List<ProductImageDTO> images)
            throws OptimisticLockException {

        Product product = this.em.find(Product.class, entityAndId.get(EntityType.PRODUCT));

        if (product == null) {
            return null;
        }

        List<Category> categoriesList = getCategoriesList(categories);
        List<ProductImage> imagesList = getImagesList(images);

        product.updateProduct(new Product(name, description, price, stock, isActive, categoriesList, imagesList));

        boolean isInserted = this.entityManagerHelper.executeMerge(this.em, product);
        if (!isInserted) {
            return null;
        }

        return product.transformToDto();
    }

    private List<Category> getCategoriesList(List<CategoryDTO> categories) {
        List<Category> categoriesList = new ArrayList<>();
        for (CategoryDTO c : categories) {
            Category category = this.em.find(Category.class, c.getCategoryId());
            categoriesList.add(category);
        }

        return categoriesList;
    }

    private List<ProductImage> getImagesList(List<ProductImageDTO> images) {
        if (images == null) {
            return null;
        }
        List<ProductImage> imagesList = new ArrayList<>();
        for (ProductImageDTO pi : images) {
            ProductImage productImage = null;
            if (pi.getProductImageId() > 0) {
                productImage = this.em.find(ProductImage.class, pi.getProductImageId());
            } else {
                productImage = new ProductImage(pi.getImageUrl(), pi.isMain());
            }
            imagesList.add(productImage);
        }

        return imagesList;
    }
}
