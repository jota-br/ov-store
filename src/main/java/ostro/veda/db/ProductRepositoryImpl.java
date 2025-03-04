package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.database.CategoryColumns;
import ostro.veda.db.helpers.database.ProductColumns;
import ostro.veda.db.helpers.database.ProductImageColumns;
import ostro.veda.db.jpa.Category;
import ostro.veda.db.jpa.Product;
import ostro.veda.db.jpa.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public ProductRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    @Transactional
    public ProductDTO add(@NonNull ProductDTO productDTO) {
        log.info("add() new Product = {}", productDTO.getName());
        List<Product> result = this.entityManagerHelper.findByFields(this.entityManager, Product.class,
                Map.of(ProductColumns.NAME.getColumnName(), productDTO.getName()));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        try {

            Product product = buildProduct(productDTO);

            this.entityManager.persist(product);
            return product.transformToDto();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public ProductDTO update(@NonNull ProductDTO productDTO) {
        log.info("update() Product = [{}, {}]", productDTO.getProductId(), productDTO.getName());
        Product product = this.entityManager.find(Product.class, productDTO.getProductId());

        try {

            product.updateProduct(buildProduct(productDTO));

            this.entityManager.merge(product);
            return product.transformToDto();
        }  catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }


    @Override
    public Product buildProduct(@NonNull ProductDTO productDTO) {
        log.info("buildProduct() Product = [{}, {}]", productDTO.getProductId(), productDTO.getName());
        return Product
                .builder()
                .productId(productDTO.getProductId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .isActive(productDTO.isActive())
                .categories(buildCategories(productDTO.getCategories()))
                .images(buildImages(productDTO.getImages()))
                .createdAt(productDTO.getCreatedAt())
                .updatedAt(productDTO.getUpdatedAt())
                .build();
    }

    @Override
    public List<Category> buildCategories(@NonNull List<CategoryDTO> categoryDTOS) {
        log.info("buildCategories() size = {}", categoryDTOS.size());
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
    public Category buildCategory(@NonNull CategoryDTO categoryDTO) {
        log.info("buildCategory() Category = [{}, {}]", categoryDTO.getCategoryId(), categoryDTO.getName());
        return Category
                .builder()
                .categoryId(categoryDTO.getCategoryId())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .isActive(categoryDTO.isActive())
                .createdAt(categoryDTO.getCreatedAt())
                .updatedAt(categoryDTO.getUpdatedAt())
                .build();
    }

    @Override
    public List<ProductImage> buildImages(@NonNull List<ProductImageDTO> productImageDTOS) {
        log.info("buildImages() size = {}", productImageDTOS.size());
        List<ProductImage> imagesList = new ArrayList<>();
        for (ProductImageDTO productImageDTO : productImageDTOS) {

            ProductImage productImage = null;

            List<ProductImage> hasProductImage = this.entityManagerHelper.findByFields(this.entityManager, ProductImage.class,
                    Map.of(ProductImageColumns.IMAGE_URL.getColumnName(), productImageDTO.getImageUrl()));

            if (hasProductImage != null) {
                productImage = hasProductImage.get(0);
                ProductImage productImageNewData = buildImage(productImageDTO);
                productImage.updateProductImage(productImageNewData);
            } else {
                productImage = buildImage(productImageDTO);
            }

            imagesList.add(productImage);
        }

        return imagesList;
    }

    @Override
    public ProductImage buildImage(@NonNull ProductImageDTO productImageDTO) {
        log.info("buildImage() ProductImage = [{}, {}]", productImageDTO.getProductImageId(), productImageDTO.getImageUrl());
        return ProductImage
                .builder()
                .productImageId(productImageDTO.getProductImageId())
                .imageUrl(productImageDTO.getImageUrl())
                .isMain(productImageDTO.isMain())
                .build();
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}