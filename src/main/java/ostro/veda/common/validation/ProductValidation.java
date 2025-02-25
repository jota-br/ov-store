package ostro.veda.common.validation;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;
import ostro.veda.common.error.ErrorHandling;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductValidation {

    /**
     * Validates and Sanitizes ProductDTO Input.
     * @param productDTO ProductDTO.
     * @return Clean ProductDTO.
     * @throws ErrorHandling.InvalidInputException On invalid Input.
     */
    public static ProductDTO validateAndSanitizeProduct(ProductDTO productDTO) throws ErrorHandling.InvalidInputException {

        int productId = productDTO.getProductId();

        String name = validateAndSanitizeName(productDTO.getName());
        String description = validateAndSanitizeDescription(productDTO.getDescription());

        double price = productDTO.getPrice();
        int stock = productDTO.getStock();
        boolean isActive = productDTO.isActive();

        List<CategoryDTO> categories = validateAndSanitizeCategories(productDTO.getCategories());
        List<ProductImageDTO> images = validateAndSanitizeImages(productDTO.getImages());

        return new ProductDTO(productId, name, description, price, stock, isActive, categories, images);
    }

    /**
     * Validates and Sanitizes Name Input.
     * @param input String.
     * @return Clean String.
     * @throws ErrorHandling.InvalidInputException
     */
    private static String validateAndSanitizeName(String input) throws ErrorHandling.InvalidInputException {
        return StringSanitizer.sanitize(CommonValidation.hasValidName(input));
    }

    /**
     * Validates and Sanitizes Description Input.
     * @param input String.
     * @return Clean String
     * @throws ErrorHandling.InvalidInputException
     */
    private static String validateAndSanitizeDescription(String input) throws ErrorHandling.InvalidInputException {
        return StringSanitizer.sanitize(CommonValidation.hasValidDescription(input));
    }

    /**
     *
     * @param categoryDTOList List with CategoryDTO.
     * @return List with CategoryDTO.
     * @throws ErrorHandling.InvalidInputException
     */
    public static List<CategoryDTO> validateAndSanitizeCategories(List<CategoryDTO> categoryDTOList) throws ErrorHandling.InvalidInputException {
        List<CategoryDTO> categories = new ArrayList<>();
        for (CategoryDTO category : categoryDTOList) {
            int categoryId = category.getCategoryId();

            String categoryName = validateAndSanitizeName(category.getName());
            String categoryDescription = category.getDescription();

            boolean categoryIsActive = category.isActive();
            categories.add(new CategoryDTO(categoryId, categoryName, categoryDescription, categoryIsActive));
        }
        return categories;
    }

    /**
     *
     * @param imageDTOList List with ProductImageDTO.
     * @return List with ProductImageDTO.
     * @throws ErrorHandling.InvalidInputException
     */
    public static List<ProductImageDTO> validateAndSanitizeImages(List<ProductImageDTO> imageDTOList) throws ErrorHandling.InvalidInputException {
        List<ProductImageDTO> images = new ArrayList<>();
        for (ProductImageDTO productImage : imageDTOList) {
            int productImageId = productImage.getProductImageId();

            String url =
                    UrlEncoder.encodeUrl(
                            hasValidImageUrl(productImage.getImageUrl()
                            ));

            boolean isMain = productImage.isMain();
            images.add(new ProductImageDTO(productImageId, url, isMain));
        }
        return images;
    }

    /**
     *
     * @param input String (URL).
     * @return Encoded String (URL).
     * @throws ErrorHandling.InvalidInputException
     */
    public static String hasValidImageUrl(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return input;
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_IMAGE_URL,
                "url:" + input
        );
    }
}
