package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductRepository extends Repository {

    private final CategoryRepository categoryRepository;

    /**
     * @param em                 EntityManager.
     * @param categoryRepository CategoryRepository.
     */
    public ProductRepository(EntityManager em, CategoryRepository categoryRepository) {
        super(em);
        this.categoryRepository = categoryRepository;
    }

    /**
     *
     * @param productDTO ProductDTO
     * @return ProductDTO
     */
    public ProductDTO addProduct(ProductDTO productDTO) throws RuntimeException {

        List<Product> result = this.entityManagerHelper.findByFields(this.em, Product.class,
                Map.of(ProductColumns.NAME.getColumnName(), productDTO.getName()));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            List<Category> categoriesList = getCategoriesList(productDTO.getCategories());
            List<ProductImage> imagesList = getImagesList(productDTO.getImages());

            Product product = new Product(productDTO.getName(), productDTO.getDescription(),
                    productDTO.getPrice(), productDTO.getStock(), productDTO.isActive(), categoriesList, imagesList);

            this.em.persist(product);

            transaction.commit();
            return product.transformToDto();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
            throw new PersistenceException("Transaction was Roll Back");
        }
    }

    /**
     * @param productDTO ProductDTO.
     * @return ProductDTO.
     * @throws OptimisticLockException
     */
    public ProductDTO updateProduct(ProductDTO productDTO)
            throws OptimisticLockException {

        Product product = this.em.find(Product.class, productDTO.getProductId());

        if (product == null) {
            return null;
        }

        List<Category> categoriesList = getCategoriesList(productDTO.getCategories());
        List<ProductImage> imagesList = getImagesList(productDTO.getImages());

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            product.updateProduct(
                    new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(),
                            productDTO.getStock(), productDTO.isActive(), categoriesList, imagesList
                    ));

            this.em.merge(product);

            transaction.commit();
        }  catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return product.transformToDto();
    }

    /**
     * Transforms CategoryDTO to Category.
     * @param categories List.
     * @return List of Category.
     */
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
