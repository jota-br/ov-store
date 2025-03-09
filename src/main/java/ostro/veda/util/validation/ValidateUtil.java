package ostro.veda.util.validation;

import org.apache.commons.validator.routines.EmailValidator;
import ostro.veda.model.dto.*;
import ostro.veda.util.enums.AddressType;
import ostro.veda.util.enums.DiscountType;
import ostro.veda.util.enums.OrderStatus;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.enums.Action;
import ostro.veda.util.constant.TableName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class ValidateUtil {

    public static void validateProduct(ProductDto productDTO) throws InputException.InvalidInputException {

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

    public static void validateCategories(List<CategoryDto> categoryDtoList) throws InputException.InvalidInputException {
        for (CategoryDto category : categoryDtoList) {
            validateCategory(category);
        }
    }

    public static void validateCategory(CategoryDto category) throws InputException.InvalidInputException {
        int categoryId = category.getCategoryId();
        String categoryName = category.getName();
        String categoryDescription = category.getDescription();

        hasValidZeroOrHigherNumber(categoryId);
        hasValidName(categoryName);
        hasValidDescription(categoryDescription);
    }

    public static void validateImages(List<ProductImageDto> imageDTOList) throws InputException.InvalidInputException {

        for (ProductImageDto productImage : imageDTOList) {
            int productImageId = productImage.getProductImageId();
            String url = productImage.getImageUrl();

            hasValidZeroOrHigherNumber(productImageId);
            hasValidImageUrl(url);
        }
    }

    public static void validateOrder(OrderDto orderDTO) throws InputException.InvalidInputException {
        hasValidZeroOrHigherNumber(orderDTO.getOrderId());
        hasValidZeroOrHigherNumber(orderDTO.getUserId());
        hasValidZeroOrHigherNumber(orderDTO.getBillingAddress().getAddressId());
        hasValidZeroOrHigherNumber(orderDTO.getShippingAddress().getAddressId());
        hasValidZeroOrHigherPrice(orderDTO.getTotalAmount());
        hasValidOrderStatus(orderDTO.getStatus());

        if (orderDTO.getCoupon() != null) validateCoupon(orderDTO.getCoupon());

        validateOrderDetail(orderDTO.getOrderDetails());
    }

    public static void validateOrderDetail(List<OrderDetailDto> orderDetailDtos) throws InputException.InvalidInputException {

        for (OrderDetailDto orderDetailDTO : orderDetailDtos) {
            hasValidZeroOrHigherNumber(orderDetailDTO.getProduct().getProductId());
            hasValidZeroOrHigherNumber(orderDetailDTO.getQuantity());
        }
    }

    public static void validateOrderIdAndStatus(int id, OrderStatus status) throws InputException.InvalidInputException {
        hasValidZeroOrHigherNumber(id);
        hasValidOrderStatus(status);
    }

    public static void validateId(int id) throws InputException.InvalidInputException {
        hasValidZeroOrHigherNumber(id);
    }

    @Deprecated
    public static void validateUser(UserDto userDTO, String password) throws InputException.InvalidInputException {
        hasValidZeroOrHigherNumber(userDTO.getUserId());
        hasValidUsername(userDTO.getUsername());
        hasValidEmail(userDTO.getEmail());
        hasValidName(userDTO.getFirstName());
        hasValidName(userDTO.getLastName());
        hasValidPhone(userDTO.getPhone());
        hasValidPassword(password);

        if (!userDTO.getAddresses().isEmpty()) validateAddress(userDTO.getAddresses());
    }

    public static void validateAddress(List<AddressDto> addressDtoList) throws InputException.InvalidInputException {
        for (AddressDto addressDTO : addressDtoList) {
            validateAddress(addressDTO);
            // needs to implement the rest of the check
            // requires Maps API implementation
        }
    }

    public static void validateAddress(AddressDto addressDTO) throws InputException.InvalidInputException {
        hasValidZeroOrHigherNumber(addressDTO.getAddressId());
        hasValidZeroOrHigherNumber(addressDTO.getUser().getUserId());
        hasValidAddressType(addressDTO.getAddressType());
        // needs to implement the rest of the check
        // requires Maps API implementation
    }

    public static void validateAudit(AuditDto auditDTO) throws InputException.InvalidInputException {
        hasValidZeroOrHigherNumber(auditDTO.getAuditId());
        hasValidAction(auditDTO.getAction().getActionName());
        hasValidChangedTable(auditDTO.getChangedTable());
    }

    public static void validateCoupon(CouponDto couponDTO) throws InputException.InvalidInputException {
        hasValidZeroOrHigherNumber(couponDTO.getCouponId());
        hasValidZeroOrHigherPrice(couponDTO.getDiscountValue());
        hasValidExpirationDate(couponDTO.getExpirationDate());
        hasValidDiscountType(couponDTO.getDiscountType());
        hasValidCode(couponDTO.getCode());
    }

    public static void hasValidName(String input) throws InputException.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[\\sA-Za-z\\p{Punct}0-9]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new InputException.InvalidInputException("Invalid Name",
                "name:" + input
        );
    }

    public static void hasValidDescription(String input) throws InputException.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z\\s\\p{Punct}0-9\n]{0,510}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new InputException.InvalidInputException("Invalid Description",
                "description:" + input
        );
    }

    public static void hasValidZeroOrHigherNumber(int number) throws InputException.InvalidInputException {
        if (number >= 0) return;

        throw new InputException.InvalidInputException(
                "Invalid Number",
                "number:" + number
        );
    }

    public static void hasValidZeroOrHigherPrice(double price) throws InputException.InvalidInputException {
        final double MINIMUM_VALID_PRICE = 0.0;
        if (price >= MINIMUM_VALID_PRICE) return;

        throw new InputException.InvalidInputException(
                "Invalid Price",
                "price:" + price
        );
    }

    public static void hasValidImageUrl(String input) throws InputException.InvalidInputException {
        Pattern validPattern = Pattern.compile("^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new InputException.InvalidInputException(
                "Invalid Image URL",
                "url:" + input
        );
    }

    private static void hasValidOrderStatus(OrderStatus status)
            throws InputException.InvalidInputException {
        if (status != null) return;

        throw new InputException.InvalidInputException(
                "Invalid Order Status", "status:null"
        );
    }

    public static void hasValidUsername(String input) throws InputException.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z0-9@_-]{8,20}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new InputException.InvalidInputException("Invalid Username", "username:" + input);
    }

    public static void hasValidPhone(String input) throws InputException.InvalidInputException {
        Pattern validPattern = Pattern.compile("\\+\\d{6,14}");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new InputException.InvalidInputException("Invalid Phone", "phone:" + input);
    }

    public static void hasValidEmail(String input) throws InputException.InvalidInputException {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (emailValidator.isValid(input)) {
            return;
        }

        throw new InputException.InvalidInputException("Invalid Email", "email:" + input);
    }

    public static void hasValidPassword(String input) throws InputException.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/|\\\\]{8,20}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return;
        }

        throw new InputException.InvalidInputException("Invalid Password", "password:*");
    }

    private static void hasValidAddressType(AddressType type) throws InputException.InvalidInputException {
        if (type != null) return;

        throw new InputException.InvalidInputException(
                "Invalid Address Type", "status:null"
        );
    }

    private static void hasValidAction(String input) throws InputException.InvalidInputException {
        for (Action action : Action.values()) {
            if (action.getActionName().equalsIgnoreCase(input)) return;
        }

        throw new InputException.InvalidInputException(
                "Invalid Action", "action:" + input
        );
    }

    private static void hasValidChangedTable(String input) throws InputException.InvalidInputException {
        for (String table : TableName.tableNameList) {
            if (table.equals(input)) return;
        }

        throw new InputException.InvalidInputException(
                "Invalid Table Name", "changedTable:" + input
        );
    }

    private static void hasValidExpirationDate(LocalDateTime input) throws InputException.InvalidInputException {
        final int MINIMUM_NUMBER_OF_HOURS_TO_EXPIRATION = 1;
        LocalDateTime minimumValidExpiration = LocalDateTime.now().plusHours(MINIMUM_NUMBER_OF_HOURS_TO_EXPIRATION);
        if (input.isAfter(minimumValidExpiration) || input.equals(minimumValidExpiration)) {
            return;
        }

        throw new InputException.InvalidInputException("Invalid Expiration Date", "expirationDate:" + input);
    }

    private static void hasValidDiscountType(DiscountType type) throws InputException.InvalidInputException {
        if (type != null) return;

        throw new InputException.InvalidInputException("Invalid Discount Type", "discountType:null");
    }

    private static void hasValidCode(String input) throws InputException.InvalidInputException {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\\-]{0,36}");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) return;

        throw new InputException.InvalidInputException("Invalid Code", "code:" + input);
    }
}
