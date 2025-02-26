package ostro.veda.common.validation;

import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.OrderStatus;

public class OrderValidation {

    public static void validateOrder(String status) throws ErrorHandling.InvalidInputException {
        hasValidOrderStatus(status);
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
