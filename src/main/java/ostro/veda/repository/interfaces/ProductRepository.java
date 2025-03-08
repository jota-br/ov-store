package ostro.veda.repository.interfaces;

import ostro.veda.model.dto.CategoryDto;
import ostro.veda.model.dto.ProductDto;
import ostro.veda.model.dto.ProductImageDto;
import ostro.veda.repository.dao.Category;
import ostro.veda.repository.dao.Product;
import ostro.veda.repository.dao.ProductImage;

import java.util.List;

public interface ProductRepository extends Repository<ProductDto> {

    Product buildProduct(ProductDto productDTO);

    List<Category> buildCategories(List<CategoryDto> categoryDtos);
    Category buildCategory(CategoryDto categoryDTO);

    List<ProductImage> buildImages(List<ProductImageDto> productImageDtos);
    ProductImage buildImage(ProductImageDto productImageDTO);
}
