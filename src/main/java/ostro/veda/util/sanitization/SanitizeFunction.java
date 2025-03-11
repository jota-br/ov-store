package ostro.veda.util.sanitization;

import ostro.veda.model.dto.CategoryDto;
import ostro.veda.model.dto.ProductDto;
import ostro.veda.model.dto.ProductImageDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SanitizeFunction<T, R> {

    R sanitize(T t);

    default String sanitizeString(String input) {

        Map<Character, String> sanitizeMap = getSanitizeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(sanitizeMap.getOrDefault(c, String.valueOf(c)));
        }

        return sb.toString();

    }

    private String encodeUrl(String input) {

        Map<Character, String> encodeMap = getEncodeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(encodeMap.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    private ProductDto sanitizeProduct(ProductDto productDTO) {

        int productId = productDTO.getProductId();
        String name = productDTO.getName();
        String description = productDTO.getDescription();
        double price = productDTO.getPrice();
        int stock = productDTO.getStock();

        name = sanitizeString(name);
        description = sanitizeString(description);

        List<CategoryDto> categories = sanitizeCategories(productDTO.getCategories());
        List<ProductImageDto> images = sanitizeImages(productDTO.getImages());

        return new ProductDto(productId, name, description, price, stock, productDTO.isActive(), categories, images,
                productDTO.getCreatedAt(), productDTO.getUpdatedAt(), productDTO.getVersion());
    }

    private List<CategoryDto> sanitizeCategories(List<CategoryDto> categoryDtoList) {

        List<CategoryDto> cleanCategoryList = new ArrayList<>();
        for (CategoryDto category : categoryDtoList) {

            cleanCategoryList.add(sanitizeCategory(category));
        }
        return cleanCategoryList;
    }

    private CategoryDto sanitizeCategory(CategoryDto category) {

        int categoryId = category.getCategoryId();
        String categoryName = category.getName();
        String categoryDescription = category.getDescription();

        categoryName = sanitizeString(categoryName);
        categoryDescription = sanitizeString(categoryDescription);

        return new CategoryDto(categoryId, categoryName, categoryDescription, category.isActive(),
                category.getCreatedAt(), category.getUpdatedAt(), category.getVersion());
    }

    private List<ProductImageDto> sanitizeImages(List<ProductImageDto> imageDTOList) {

        List<ProductImageDto> cleanProductImageList = new ArrayList<>();
        for (ProductImageDto productImage : imageDTOList) {

            int productImageId = productImage.getProductImageId();
            String url = productImage.getImageUrl();
            url = encodeUrl(url);

            cleanProductImageList.add(new ProductImageDto(productImageId, url, productImage.isMain(), productImage.getVersion()));
        }
        return cleanProductImageList;
    }

    private Map<Character, String> getSanitizeMap() {

        Map<Character, String> sanitize = new HashMap<>();
        sanitize.put('\'', "&apos;");
        sanitize.put('&', "&amp;");
        sanitize.put(';', "\\;");
        sanitize.put('/', "\\/");
        sanitize.put('\\', "\\\\");
        sanitize.put('(', "\\(");
        sanitize.put(')', "\\)");
        sanitize.put('{', "\\{");
        sanitize.put('}', "\\}");
        sanitize.put('<', "&lt;");
        sanitize.put('>', "&gt;");
        sanitize.put('\"', "&quot;");
        sanitize.put('%', "\\%");
        sanitize.put('|', "\\|");
        sanitize.put('^', "\\^");
        sanitize.put('$', "\\$");
        return sanitize;
    }

    private Map<Character, String> getEncodeMap() {

        Map<Character, String> encodingMap = new HashMap<>();
        encodingMap.put(' ', "%20");
        encodingMap.put('!', "%21");
        encodingMap.put('\"', "%22");
        encodingMap.put('#', "%23");
        encodingMap.put('$', "%24");
        encodingMap.put('%', "%25");
        encodingMap.put('&', "%26");
        encodingMap.put('\'', "%27");
        encodingMap.put('(', "%28");
        encodingMap.put(')', "%29");
        encodingMap.put('*', "%2A");
        encodingMap.put('+', "%2B");
        encodingMap.put(',', "%2C");
        encodingMap.put('-', "%2D");
//        encodingMap.put('.', "%2E");
        encodingMap.put(';', "%3B");
        encodingMap.put('<', "%3C");
//        encodingMap.put('=', "%3D");
        encodingMap.put('>', "%3E");
//        encodingMap.put('?', "%3F");
        encodingMap.put('@', "%40");
        encodingMap.put('[', "%5B");
        encodingMap.put('\\', "%5C");
        encodingMap.put(']', "%5D");
        encodingMap.put('^', "%5E");
        encodingMap.put('_', "%5F");
        encodingMap.put('`', "%60");
        encodingMap.put('{', "%7B");
        encodingMap.put('|', "%7C");
        encodingMap.put('}', "%7D");
        encodingMap.put('~', "%7E");
        // encodingMap.put('/', "%2F");
        // encodingMap.put(':', "%3A");
        return encodingMap;
    }
}
