package ostro.veda.common.validation;

import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Product;

import java.util.Map;

public class OrderValidation {

    private static final double MINIMUM_TOTAL_AMOUNT = 0.0;
    private static final int MINIMUM_VALID_QUANTITY = 1, MINIMUM_VALID_ID = 0;

    public static boolean hasValidInput(int userId, double totalAmount, String status, AddressDTO shippingAddress,
                                        AddressDTO billingAddress, Map<ProductDTO, Integer> productAndQuantity)
            throws ErrorHandling.InvalidInputException {
        return hasValidTotalAmount(totalAmount) && hasValidOrderStatus(status) &&
                hasValidAddress(userId, billingAddress) && hasValidAddress(userId, shippingAddress) &&
                hasValidProductAndQuantity(productAndQuantity);
    }

    public static boolean hasValidInput(int orderId, String status) throws ErrorHandling.InvalidInputException {
        return hasValidOrderStatus(status) && hasValidId(orderId);
    }

    public static boolean hasValidInput(Product product, int quantity) throws ErrorHandling.InvalidInputException {
        return hasValidProductAndQuantity(product, quantity);
    }

    public static boolean hasValidInput(int orderId) throws ErrorHandling.InvalidInputException {
        return hasValidId(orderId);
    }

    private static boolean hasValidTotalAmount(double amount) throws ErrorHandling.InvalidInputException {
        if (amount >= MINIMUM_TOTAL_AMOUNT) {
            return true;
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_TOTAL_AMOUNT, String.valueOf(amount)
        );
    }

    private static boolean hasValidOrderStatus(String status)
            throws ErrorHandling.InvalidInputException {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equals(status)) {
                return true;
            }
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_ORDER_STATUS, status
        );
    }

    private static boolean hasValidAddress(int userId, AddressDTO address) throws ErrorHandling.InvalidInputException {
        if (address != null && address.getUserId() == userId) {
            return true;
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_ADDRESS, "address is null or userId doesn't match"
        );
    }

    private static boolean hasValidProductAndQuantity(Map<ProductDTO, Integer> productAndQuantity)
            throws ErrorHandling.InvalidInputException {
        for (Map.Entry<ProductDTO, Integer> entry : productAndQuantity.entrySet()) {
            ProductDTO productDTO = entry.getKey();
            int quantity = entry.getValue();
            if (productDTO == null) {
                throw new ErrorHandling.InvalidInputException(
                        ErrorHandling.InputExceptionMessage.EX_INVALID_PRODUCT, "null"
                );
            } else if (quantity < MINIMUM_VALID_QUANTITY || productDTO.getStock() < quantity) {
                throw new ErrorHandling.InvalidInputException(
                        ErrorHandling.InputExceptionMessage.EX_INVALID_PRODUCT_QUANTITY,
                        "quantity:" + quantity + ", stock:" + productDTO.getStock()
                );
            }
        }
        return true;
    }

    private static boolean hasValidProductAndQuantity(Product product, int quantity)
            throws ErrorHandling.InvalidInputException {
        if (product == null) {
            throw new ErrorHandling.InvalidInputException(
                    ErrorHandling.InputExceptionMessage.EX_INVALID_PRODUCT, "null"
            );
        } else if (quantity < MINIMUM_VALID_QUANTITY || product.getStock() < quantity) {
            throw new ErrorHandling.InvalidInputException(
                    ErrorHandling.InputExceptionMessage.EX_INVALID_PRODUCT_QUANTITY,
                    "quantity:" + quantity + ", stock:" + product.getStock()
            );
        }
        return true;
    }

    private static boolean hasValidId(int input) throws ErrorHandling.InvalidInputException {
        if (input > MINIMUM_VALID_ID) return true;
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_ID,
                "id:" + input
        );
    }
}
