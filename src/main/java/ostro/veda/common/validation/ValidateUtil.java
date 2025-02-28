package ostro.veda.common.validation;

import org.apache.commons.validator.routines.EmailValidator;
import ostro.veda.common.dto.*;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.AddressType;
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
            validateCategory(category);
        }
    }

    public static void validateCategory(CategoryDTO category) throws ErrorHandling.InvalidInputException {
        int categoryId = category.getCategoryId();
        String categoryName = category.getName();
        String categoryDescription = category.getDescription();

        hasValidZeroOrHigherNumber(categoryId);
        hasValidName(categoryName);
        hasValidDescription(categoryDescription);
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

    public static void validateUser(UserDTO userDTO, String password) throws ErrorHandling.InvalidInputException {
        hasValidZeroOrHigherNumber(userDTO.getUserId());
        hasValidUsername(userDTO.getUsername());
        hasValidEmail(userDTO.getEmail());
        hasValidName(userDTO.getFirstName());
        hasValidName(userDTO.getLastName());
        hasValidPhone(userDTO.getPhone());
        hasValidPassword(password);

        if (!userDTO.getAddresses().isEmpty()) validateAddress(userDTO.getAddresses());
    }

    public static void validateAddress(List<AddressDTO> addressDTOList) throws ErrorHandling.InvalidInputException {
        for (AddressDTO addressDTO : addressDTOList) {
            validateAddress(addressDTO);
            // needs to implement the rest of the check
            // requires Maps API implementation
        }
    }

    public static void validateAddress(AddressDTO addressDTO) throws ErrorHandling.InvalidInputException {
        hasValidZeroOrHigherNumber(addressDTO.getAddressId());
        hasValidZeroOrHigherNumber(addressDTO.getUserId());
        hasValidAddressType(addressDTO.getAddressType());
        // needs to implement the rest of the check
        // requires Maps API implementation
    }

    public static void hasValidName(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[\\sA-Za-z\\p{Punct}]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException("Invalid Name",
                "name:" + input
        );
    }

    public static void hasValidDescription(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z\\s\\p{Punct}\n]{0,510}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException("Invalid Description",
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
                "Invalid Image URL",
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
                "Invalid Order Status", "status:" + status
        );
    }

    public static void hasValidUsername(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z0-9@_-]{8,20}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException("Invalid Username", "username:" + input);
    }

    public static void hasValidPhone(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("\\+\\d{6,14}");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException("Invalid Phone", "phone:" + input);
    }

    public static void hasValidEmail(String input) throws ErrorHandling.InvalidInputException {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (emailValidator.isValid(input)) {
            return;
        }

        throw new ErrorHandling.InvalidInputException("Invalid Email", "email:" + input);
    }

    public static void hasValidPassword(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/|\\\\]{8,20}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new ErrorHandling.InvalidInputException("Invalid Password", "password:*");
    }

    private static void hasValidAddressType(String type)
            throws ErrorHandling.InvalidInputException {
        for (AddressType addressType : AddressType.values()) {
            if (addressType.getValue().equals(type)) {
                return;
            }
        }

        throw new ErrorHandling.InvalidInputException(
                "Invalid Address Type", "status:" + type
        );
    }
}
