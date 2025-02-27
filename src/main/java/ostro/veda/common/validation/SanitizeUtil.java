package ostro.veda.common.validation;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.common.dto.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanitizeUtil {

    public static ProductDTO sanitizeProduct(ProductDTO productDTO) {

        int productId = productDTO.getProductId();
        String name = productDTO.getName();
        String description = productDTO.getDescription();
        double price = productDTO.getPrice();
        int stock = productDTO.getStock();

        name = sanitize(name);
        description = sanitize(description);

        List<CategoryDTO> categories = sanitizeCategories(productDTO.getCategories());
        List<ProductImageDTO> images = sanitizeImages(productDTO.getImages());

        return new ProductDTO(productId, name, description, price, stock, productDTO.isActive(), categories, images);
    }

    public static List<CategoryDTO> sanitizeCategories(List<CategoryDTO> categoryDTOList) {

        List<CategoryDTO> cleanCategoryList = new ArrayList<>();
        for (CategoryDTO category : categoryDTOList) {

            int categoryId = category.getCategoryId();
            String categoryName = category.getName();
            String categoryDescription = category.getDescription();

            categoryName = sanitize(categoryName);
            categoryDescription = sanitize(categoryDescription);

            cleanCategoryList.add(new CategoryDTO(categoryId, categoryName, categoryDescription, category.isActive()));
        }
        return cleanCategoryList;
    }

    public static List<ProductImageDTO> sanitizeImages(List<ProductImageDTO> imageDTOList) {

        List<ProductImageDTO> cleanProductImageList = new ArrayList<>();
        for (ProductImageDTO productImage : imageDTOList) {

            int productImageId = productImage.getProductImageId();
            String url = productImage.getImageUrl();
            url = encodeUrl(url);

            cleanProductImageList.add(new ProductImageDTO(productImageId, url, productImage.isMain()));
        }
        return cleanProductImageList;
    }

    public static UserDTO sanitizeUser(UserDTO userDTO) {
        return null;
    }

    public static String sanitize(String input) {
        Map<Character, String> sanitize = getSanitizeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(sanitize.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    private static Map<Character, String> getSanitizeMap() {
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

    public static String encodeUrl(String input) {
        Map<Character, String> encodingMap = getEncodeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(encodingMap.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    public static Map<Character, String> getEncodeMap() {
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
