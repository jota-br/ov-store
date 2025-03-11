package ostro.veda.util.sanitization;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.model.dto.CategoryDto;
import ostro.veda.model.dto.ProductDto;
import ostro.veda.model.dto.ProductImageDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SanitizeProduct implements SanitizeFunction<ProductDto, ProductDto> {

    @Override
    public ProductDto sanitize(ProductDto productDto) {

        log.info("Sanitizing = {}", productDto.getClass().getSimpleName());

        int productId = productDto.getProductId();
        String name = productDto.getName();
        String description = productDto.getDescription();
        double price = productDto.getPrice();
        int stock = productDto.getStock();

        name = sanitizeString(name);
        description = sanitizeString(description);

        SanitizeCategory sanitizeCategory = new SanitizeCategory();
        List<CategoryDto> categories = new ArrayList<>();

        for (CategoryDto categoryDto : productDto.getCategories()) {
            categories.add(sanitizeCategory.sanitize(categoryDto));
        }

        SanitizeProductImage sanitizeProductImage = new SanitizeProductImage();
        List<ProductImageDto> images = new ArrayList<>();

        for (ProductImageDto productImageDto : productDto.getImages()) {
            images.add(sanitizeProductImage.sanitize(productImageDto));
        }

        return new ProductDto(productId, name, description, price, stock, productDto.isActive(), categories, images,
                productDto.getCreatedAt(), productDto.getUpdatedAt(), productDto.getVersion());
    }
}
