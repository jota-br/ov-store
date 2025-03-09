package ostro.veda.util.validation;

import ostro.veda.model.dto.*;
import ostro.veda.util.enums.AddressType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanitizeUtil {

    public static ProductDto sanitizeProduct(ProductDto productDTO) {

        int productId = productDTO.getProductId();
        String name = productDTO.getName();
        String description = productDTO.getDescription();
        double price = productDTO.getPrice();
        int stock = productDTO.getStock();

        name = sanitize(name);
        description = sanitize(description);

        List<CategoryDto> categories = sanitizeCategories(productDTO.getCategories());
        List<ProductImageDto> images = sanitizeImages(productDTO.getImages());

        return new ProductDto(productId, name, description, price, stock, productDTO.isActive(), categories, images,
                productDTO.getCreatedAt(), productDTO.getUpdatedAt(), productDTO.getVersion());
    }

    public static List<CategoryDto> sanitizeCategories(List<CategoryDto> categoryDtoList) {

        List<CategoryDto> cleanCategoryList = new ArrayList<>();
        for (CategoryDto category : categoryDtoList) {

            cleanCategoryList.add(sanitizeCategory(category));
        }
        return cleanCategoryList;
    }

    public static CategoryDto sanitizeCategory(CategoryDto category) {

        int categoryId = category.getCategoryId();
        String categoryName = category.getName();
        String categoryDescription = category.getDescription();

        categoryName = sanitize(categoryName);
        categoryDescription = sanitize(categoryDescription);

        return new CategoryDto(categoryId, categoryName, categoryDescription, category.isActive(),
                category.getCreatedAt(), category.getUpdatedAt(), category.getVersion());
    }

    public static List<ProductImageDto> sanitizeImages(List<ProductImageDto> imageDTOList) {

        List<ProductImageDto> cleanProductImageList = new ArrayList<>();
        for (ProductImageDto productImage : imageDTOList) {

            int productImageId = productImage.getProductImageId();
            String url = productImage.getImageUrl();
            url = encodeUrl(url);

            cleanProductImageList.add(new ProductImageDto(productImageId, url, productImage.isMain(), productImage.getVersion()));
        }
        return cleanProductImageList;
    }

    public static UserDto sanitizeUser(UserDto userDTO) {

        String firstName = sanitize(userDTO.getFirstName());
        String lastName = sanitize(userDTO.getLastName());
        String username = sanitize(userDTO.getUsername());
        String email = sanitize(userDTO.getEmail());
        List<AddressDto> addressDtoList = sanitizeAddress(userDTO.getAddresses());

        return new UserDto(userDTO.getUserId(), username, userDTO.getSalt(), userDTO.getHash(), email, firstName,
                lastName, userDTO.getPhone(), userDTO.isActive(), userDTO.getRole(), addressDtoList,
                userDTO.getCreatedAt(), userDTO.getUpdatedAt(), userDTO.getVersion());
    }

    public static List<AddressDto> sanitizeAddress(List<AddressDto> addressDtoList) {

        List<AddressDto> addressDtos = new ArrayList<>();
        for (AddressDto addressDTO : addressDtoList) {
            addressDtos.add(sanitizeAddress(addressDTO));
        }
        return addressDtos;
    }

    public static AddressDto sanitizeAddress(AddressDto addressDTO) {

        int id = addressDTO.getAddressId();
        UserDto userDTO = addressDTO.getUser();
        String streetAddress = sanitize(addressDTO.getStreetAddress());
        String addressNumber = sanitize(addressDTO.getAddressNumber());
        AddressType addressType = addressDTO.getAddressType();
        String city = sanitize(addressDTO.getCity());
        String state = sanitize(addressDTO.getState());
        String zipCode = sanitize(addressDTO.getZipCode());
        String country = sanitize(addressDTO.getCountry());
        boolean isActive = addressDTO.isActive();

        return new AddressDto(id, userDTO, streetAddress, addressNumber, addressType, city, state, zipCode,
                country, isActive, addressDTO.getCreatedAt(), addressDTO.getUpdatedAt(), addressDTO.getVersion());
    }

    public static AuditDto sanitizeAudit(AuditDto auditDTO) {

            String changedData = sanitize(auditDTO.getChangedData());
            return new AuditDto(auditDTO.getAuditId(), auditDTO.getAction(), auditDTO.getChangedTable(),
                    changedData, auditDTO.getChangedAt(), auditDTO.getChangedBy(), auditDTO.getUserId());

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
