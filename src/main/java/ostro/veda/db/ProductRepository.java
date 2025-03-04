package ostro.veda.db;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.db.jpa.Category;
import ostro.veda.db.jpa.Product;
import ostro.veda.db.jpa.ProductImage;

import java.util.List;

public interface ProductRepository extends Repository<ProductDTO> {

    Product buildProduct(ProductDTO productDTO);

    List<Category> buildCategories(List<CategoryDTO> categoryDTOS);
    Category buildCategory(CategoryDTO categoryDTO);

    List<ProductImage> buildImages(List<ProductImageDTO> productImageDTOS);
    ProductImage buildImage(ProductImageDTO productImageDTO);
}
