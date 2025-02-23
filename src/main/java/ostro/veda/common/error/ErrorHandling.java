package ostro.veda.common.error;

public class ErrorHandling {

    public static class InvalidInputException extends Exception {
        public InvalidInputException(InputExceptionMessage e, String input) {
            super(e.getMessage() + " [ Input: {" + input + "} ]");
        }
    }

    public enum InputExceptionMessage {

        EX_INVALID_TOTAL_AMOUNT("Invalid Total Amount"),
        EX_INVALID_ORDER_STATUS("Invalid Order Status"),
        EX_INVALID_ADDRESS("Invalid Address"),
        EX_INVALID_PRODUCT("Invalid Product"),
        EX_INVALID_PRODUCT_QUANTITY("Invalid Product Quantity"),
        EX_INVALID_ID("Invalid Database Id"),
        EX_INVALID_ORDER_RETURN("Invalid Input for Order Return Request"),
        EX_INVALID_ORDER_RETURN_DS("Return is unavailable, check product Order Date and Status");

        private final String message;

        InputExceptionMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

    public static class InvalidUsernameException extends Exception {
        public InvalidUsernameException() {
            super("Username is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidPasswordException extends Exception {
        public InvalidPasswordException() {
            super("Password is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidLengthException extends Exception {
        public InvalidLengthException() {
            super("Input is out of range. Check field requirements and restrictions for valid input length.");
        }
    }

    public static class InvalidNameException extends Exception {
        public InvalidNameException() {
            super("Name is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidDescriptionException extends Exception {
        public InvalidDescriptionException() {
            super("Description is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidImageUrlException extends Exception {
        public InvalidImageUrlException() {
            super("Image URL is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidEmailException extends Exception {
        public InvalidEmailException() {
            super("Email is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidPhoneException extends Exception {
        public InvalidPhoneException() {
            super("Phone is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidStreetAddressException extends Exception {
        public InvalidStreetAddressException() {
            super("Street Address is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidAddressNumberException extends Exception {
        public InvalidAddressNumberException() {
            super("Address Number is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidAddressTypeException extends Exception {
        public InvalidAddressTypeException() {
            super("Address Type is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidPersonNameException extends Exception {
        public InvalidPersonNameException() {
            super("Name is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidValueException extends Exception {
        public InvalidValueException() {
            super("Value is invalid. Values and prices can't be negative.");
        }
    }

    public static class InvalidOrderStatusException extends Exception {
        public InvalidOrderStatusException() {
            super("Order Status is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidAddressException extends Exception {
        public InvalidAddressException() {
            super("Address is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidProductException extends Exception {
        public InvalidProductException() {
            super("Product is invalid. Product is null or doesn't exists.");
        }
    }

    public static class InvalidQuantityException extends Exception {
        public InvalidQuantityException() {
            super("Quantity is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidProcessDataTypeException extends Exception {
        public InvalidProcessDataTypeException() {
            super("Process Data Type is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InsufficientInventoryException extends Exception {
        public InsufficientInventoryException() {
            super("Insufficient Inventory. Stock quantity is insufficient to supply the order.");
        }
    }

    public static class UnableToPersistException extends Exception {
        public UnableToPersistException() {
            super("Unable to Persist Changes. Something went wrong and all changes were rolled back.");
        }
    }
}
