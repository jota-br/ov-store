package ostro.veda.common.validation;

import ostro.veda.common.dto.*;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.OrderStatus;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

    public static void validateProduct(ProductDTO productDTO) throws ErrorHandling.InvalidInputException {

        int productId = productDTO.getProductId();
        String name = productDTO.getName();
        String description = productDTO.getDescription();
        double price = productDTO.getPrice();
        int stock = productDTO.getStock();

        hasValidZeroOrHigherNumber(productId);
        hasValidName(name);
        hasValidDescription(description);
        hasValidZeroOrHigherPrice(price);
        hasValidZeroOrHigherNumber(stock);

        validateCategories(productDTO.getCategories());
        validateImages(productDTO.getImages());
    }

    public static void validateCategories(List<CategoryDTO> categoryDTOList) throws ErrorHandling.InvalidInputException {
        for (CategoryDTO category : categoryDTOList) {

            int categoryId = category.getCategoryId();
            String categoryName = category.getName();
            String categoryDescription = category.getDescription();

            hasValidZeroOrHigherNumber(categoryId);
            hasValidName(categoryName);
            hasValidDescription(categoryDescription);
        }
    }

    public static void validateImages(List<ProductImageDTO> imageDTOList) throws ErrorHandling.InvalidInputException {

        for (ProductImageDTO productImage : imageDTOList) {
            int productImageId = productImage.getProductImageId();
            String url = productImage.getImageUrl();

            hasValidZeroOrHigherNumber(productImageId);
            hasValidImageUrl(url);
        }
    }

    public static void validateOrder(OrderBasic orderBasic) throws ErrorHandling.InvalidInputException {
        hasValidZeroOrHigherNumber(orderBasic.getOrderId());
        hasValidZeroOrHigherNumber(orderBasic.getUserId());
        hasValidZeroOrHigherNumber(orderBasic.getBillingAddressId());
        hasValidZeroOrHigherNumber(orderBasic.getShippingAddressId());
        hasValidZeroOrHigherPrice(orderBasic.getTotalAmount());
        hasValidOrderStatus(orderBasic.getStatus());

        validateOrderDetail(orderBasic.getOrderDetails());
    }

    public static void validateOrderDetail(List<OrderDetailBasic> orderDetailBasicList) throws ErrorHandling.InvalidInputException {

        for (OrderDetailBasic orderDetailBasic : orderDetailBasicList) {
            hasValidZeroOrHigherNumber(orderDetailBasic.getProductId());
            hasValidZeroOrHigherNumber(orderDetailBasic.getQuantity());
        }
    }

    public static void validateOrderIdAndStatus(int id, String status) throws ErrorHandling.InvalidInputException {
        hasValidZeroOrHigherNumber(id);
        hasValidOrderStatus(status);
    }

    public static void validateId(int id) throws ErrorHandling.InvalidInputException {
        hasValidZeroOrHigherNumber(id);
    }

    public static void hasValidName(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[\\sA-Za-z\\p{Punct}]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_NAME,
                "name:" + input
        );
    }

    public static void hasValidDescription(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z\\s\\p{Punct}\n]{0,510}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_DESCRIPTION,
                "description:" + input
        );
    }

    public static void hasValidZeroOrHigherNumber(int number) throws ErrorHandling.InvalidInputException {
        if (number >= 0) return;

        throw new ErrorHandling.InvalidInputException(
                "Invalid Number",
                "number:" + number
        );
    }

    public static void hasValidZeroOrHigherPrice(double price) throws ErrorHandling.InvalidInputException {
        if (price >= 0.0) return;

        throw new ErrorHandling.InvalidInputException(
                "Invalid Price",
                "price:" + price
        );
    }

    public static void hasValidImageUrl(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_IMAGE_URL,
                "url:" + input
        );
    }

    private static void hasValidOrderStatus(String status)
            throws ErrorHandling.InvalidInputException {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equals(status)) {
                return;
            }
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_ORDER_STATUS, status
        );
    }
}
