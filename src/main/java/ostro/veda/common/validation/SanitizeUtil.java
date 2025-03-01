package ostro.veda.common.validation;

import ostro.veda.common.dto.*;

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

        return new ProductDTO(productId, name, description, price, stock, productDTO.isActive(), categories, images,
                productDTO.getCreatedAt(), productDTO.getUpdatedAt(), productDTO.getVersion());
    }

    public static List<CategoryDTO> sanitizeCategories(List<CategoryDTO> categoryDTOList) {

        List<CategoryDTO> cleanCategoryList = new ArrayList<>();
        for (CategoryDTO category : categoryDTOList) {

            cleanCategoryList.add(sanitizeCategory(category));
        }
        return cleanCategoryList;
    }

    public static CategoryDTO sanitizeCategory(CategoryDTO category) {

        int categoryId = category.getCategoryId();
        String categoryName = category.getName();
        String categoryDescription = category.getDescription();

        categoryName = sanitize(categoryName);
        categoryDescription = sanitize(categoryDescription);

        return new CategoryDTO(categoryId, categoryName, categoryDescription, category.isActive(),
                category.getCreatedAt(), category.getUpdatedAt(), category.getVersion());
    }

    public static List<ProductImageDTO> sanitizeImages(List<ProductImageDTO> imageDTOList) {

        List<ProductImageDTO> cleanProductImageList = new ArrayList<>();
        for (ProductImageDTO productImage : imageDTOList) {

            int productImageId = productImage.getProductImageId();
            String url = productImage.getImageUrl();
            url = encodeUrl(url);

            cleanProductImageList.add(new ProductImageDTO(productImageId, url, productImage.isMain(), productImage.getVersion()));
        }
        return cleanProductImageList;
    }

    public static UserDTO sanitizeUser(UserDTO userDTO) {

        String firstName = sanitize(userDTO.getFirstName());
        String lastName = sanitize(userDTO.getLastName());
        String username = sanitize(userDTO.getUsername());
        String email = sanitize(userDTO.getEmail());
        List<AddressDTO> addressDTOList = sanitizeAddress(userDTO.getAddresses());

        return new UserDTO(userDTO.getUserId(), username, userDTO.getSalt(), userDTO.getHash(), email, firstName,
                lastName, userDTO.getPhone(), userDTO.isActive(), userDTO.getRole(), addressDTOList,
                userDTO.getCreatedAt(), userDTO.getUpdatedAt(), userDTO.getVersion());
    }

    public static List<AddressDTO> sanitizeAddress(List<AddressDTO> addressDTOList) {

        List<AddressDTO> addressDTOS = new ArrayList<>();
        for (AddressDTO addressDTO : addressDTOList) {
            addressDTOS.add(sanitizeAddress(addressDTO));
        }
        return addressDTOS;
    }

    public static AddressDTO sanitizeAddress(AddressDTO addressDTO) {

        int id = addressDTO.getAddressId();
        int userId = addressDTO.getUserId();
        String streetAddress = sanitize(addressDTO.getStreetAddress());
        String addressNumber = sanitize(addressDTO.getAddressNumber());
        String addressType = sanitize(addressDTO.getAddressType());
        String city = sanitize(addressDTO.getCity());
        String state = sanitize(addressDTO.getState());
        String zipCode = sanitize(addressDTO.getZipCode());
        String country = sanitize(addressDTO.getCountry());
        boolean isActive = addressDTO.isActive();

        return new AddressDTO(id, userId, streetAddress, addressNumber, addressType, city, state, zipCode,
                country, isActive, addressDTO.getCreatedAt(), addressDTO.getUpdatedAt(), addressDTO.getVersion());
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
