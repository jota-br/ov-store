package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OptimisticLockException;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.CategoryColumns;
import ostro.veda.db.helpers.columns.ProductColumns;
import ostro.veda.db.helpers.columns.ProductImageColumns;
import ostro.veda.db.jpa.Category;
import ostro.veda.db.jpa.Product;
import ostro.veda.db.jpa.ProductImage;
import ostro.veda.loggerService.Logger;

import java.util.*;

public class ProductRepository extends Repository {

    private final CategoryRepository categoryRepository;

    public ProductRepository(EntityManager em, CategoryRepository categoryRepository) {
        super(em);
        this.categoryRepository = categoryRepository;
    }

    public ProductDTO addProduct(String name, String description, double price, int stock, boolean isActive,
                                 List<CategoryDTO> categories, List<ProductImageDTO> images) {

        List<Product> result = this.entityManagerHelper.findByFields(this.em, Product.class,
                Map.of(ProductColumns.NAME.getColumnName(), name));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        ProductDTO productDTO = null;
        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            List<Category> categoriesList = getCategoriesList(categories);
            List<ProductImage> imagesList = getImagesList(images);
            Product product = new Product(name, description, price, stock, isActive, categoriesList, imagesList);

            this.em.persist(product);

            transaction.commit();
            productDTO = product.transformToDto();
        } catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
        }

        return productDTO;
    }

    public ProductDTO updateProduct(int productId, String name, String description, double price, int stock, boolean isActive,
                                           List<CategoryDTO> categories, List<ProductImageDTO> images)
            throws OptimisticLockException {

        Product product = this.em.find(Product.class, productId);

        if (product == null) {
            return null;
        }

        List<Category> categoriesList = getCategoriesList(categories);
        List<ProductImage> imagesList = getImagesList(images);

        List<Boolean> booleanList = new ArrayList<>(categoriesList.size());
        categoryRepository.updateProduct(categories);

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
            Category category = null;
            List<Category> result = this.entityManagerHelper.findByFields(this.getEm(), Category.class,
                    Map.of(CategoryColumns.NAME.getColumnName(), c.getName()));
            if (result != null) {
                category = result.get(0);
                category.updateCategory(new Category(c.getName(), c.getDescription(), c.isActive()));
            } else {
                category = new Category(c.getName(), c.getDescription(), c.isActive());
            }
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
            List<ProductImage> result = this.entityManagerHelper.findByFields(this.getEm(), ProductImage.class,
                    Map.of(ProductImageColumns.IMAGE_URL.getColumnName(), pi.getImageUrl()));
            if (result != null) {
                productImage = result.get(0);
                productImage.updateProductImage(new ProductImage(pi.getImageUrl(), pi.isMain()));
            } else {
                productImage = new ProductImage(pi.getImageUrl(), pi.isMain());
            }
            imagesList.add(productImage);
        }

        return imagesList;
    }
}
