package main.java.ostro.veda.db;

import main.java.ostro.veda.common.dto.CategoryDTO;
import main.java.ostro.veda.common.dto.ProductDTO;
import main.java.ostro.veda.common.dto.ProductImageDTO;
import main.java.ostro.veda.db.jpa.Category;
import main.java.ostro.veda.db.jpa.Product;
import main.java.ostro.veda.db.jpa.ProductImage;

import java.util.List;

public interface ProductRepository extends Repository<ProductDTO> {

    Product buildProduct(ProductDTO productDTO);

    List<Category> buildCategories(List<CategoryDTO> categoryDTOS);
    Category buildCategory(CategoryDTO categoryDTO);

    List<ProductImage> buildImages(List<ProductImageDTO> productImageDTOS);
    ProductImage buildImage(ProductImageDTO productImageDTO);
}
