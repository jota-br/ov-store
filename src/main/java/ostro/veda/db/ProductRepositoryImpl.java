package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.CategoryColumns;
import ostro.veda.db.helpers.columns.ProductColumns;
import ostro.veda.db.helpers.columns.ProductImageColumns;
import ostro.veda.db.jpa.Category;
import ostro.veda.db.jpa.Product;
import ostro.veda.db.jpa.ProductImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final EntityManager entityManager;
    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public ProductRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        this.entityManager = entityManager;
        this.entityManagerHelper = entityManagerHelper;
    }

    private List<Category> getCategoriesList(List<CategoryDTO> categories) {

    }

    /**
     * Transforms ProductImageDTO to ProductImage.
     * @param images List.
     * @return List of ProductImage.
     */
    private List<ProductImage> getImagesList(List<ProductImageDTO> images) {
        if (images == null) {
            return null;
        }
        List<ProductImage> imagesList = new ArrayList<>();
        for (ProductImageDTO pi : images) {

            ProductImage productImage = null;

            List<ProductImage> result = this.entityManagerHelper.findByFields(this.getEm(), ProductImage.class,
                    Map.of(ProductImageColumns.IMAGE_URL.getColumnName(), pi.getImageUrl()));

            ProductImage newProductImage = new ProductImage(pi.getProductImageId(), pi.getImageUrl(), pi.isMain(), pi.getVersion());
            if (result != null) {
                productImage = result.get(0);
                productImage.updateProductImage(newProductImage);
            } else {
                productImage = newProductImage;
            }

            imagesList.add(productImage);
        }

        return imagesList;
    }

    @Override
    public ProductDTO add(ProductDTO productDTO) {
        List<Product> result = this.entityManagerHelper.findByFields(this.entityManager, Product.class,
                Map.of(ProductColumns.NAME.getColumnName(), productDTO.getName()));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            List<Category> categoriesList = getCategoriesList(productDTO.getCategories());
            List<ProductImage> imagesList = getImagesList(productDTO.getImages());

            Product product = new Product(0, productDTO.getName(), productDTO.getDescription(),
                    productDTO.getPrice(), productDTO.getStock(), productDTO.isActive(), categoriesList, imagesList, null, null, 0);

            this.entityManager.persist(product);

            transaction.commit();
            return product.transformToDto();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
            throw new PersistenceException("Transaction was Rolled Back");
        }
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        Product product = this.entityManager.find(Product.class, productDTO.getProductId());

        if (product == null) {
            return null;
        }

        List<Category> categoriesList = buildCategories(productDTO.getCategories());
        List<ProductImage> imagesList = buildImages(productDTO.getImages());

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            product.updateProduct(
                    new Product(0, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(),
                            productDTO.getStock(), productDTO.isActive(), categoriesList, imagesList, null, null, 0));

            this.entityManager.merge(product);

            transaction.commit();
        }  catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return product.transformToDto();
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    @Override
    public List<Category> buildCategories(List<CategoryDTO> categoryDTOS) {
        List<Category> categoriesList = new ArrayList<>();
        for (CategoryDTO categoryDTO : categoryDTOS) {

            Category category = null;

            List<Category> hasCategory = this.entityManagerHelper.findByFields(this.entityManager, Category.class,
                    Map.of(CategoryColumns.NAME.getColumnName(), categoryDTO.getName()));

            if (hasCategory != null) {
                category = hasCategory.get(0);
                Category categoryNewData = buildCategory(categoryDTO);
                category.updateCategory(categoryNewData);
            } else {
                category = buildCategory(categoryDTO);
            }

            categoriesList.add(category);
        }

        return categoriesList;
    }

    @Override
    public Category buildCategory(CategoryDTO categoryDTO) {
        return new Category()
                .setCategoryId(categoryDTO.getCategoryId())
                .setName(categoryDTO.getName())
                .setDescription(categoryDTO.getDescription())
                .setActive(categoryDTO.isActive())
                .setCreatedAt(categoryDTO.getCreatedAt())
                .setUpdatedAt(categoryDTO.getUpdatedAt());
    }

    @Override
    public List<ProductImage> buildImages(@NonNull List<ProductImageDTO> productImageDTOS) {
        List<ProductImage> imagesList = new ArrayList<>();
        for (ProductImageDTO productImageDTO : productImageDTOS) {

            ProductImage productImage = null;

            List<ProductImage> result = this.entityManagerHelper.findByFields(this.entityManager, ProductImage.class,
                    Map.of(ProductImageColumns.IMAGE_URL.getColumnName(), productImageDTO.getImageUrl()));

            ProductImage newProductImage = new ProductImage(productImageDTO.getProductImageId(), productImageDTO.getImageUrl(), productImageDTO.isMain(), productImageDTO.getVersion());
            if (result != null) {
                productImage = result.get(0);
                productImage.updateProductImage(newProductImage);
            } else {
                productImage = newProductImage;
            }

            imagesList.add(productImage);
        }

        return imagesList;
    }
}