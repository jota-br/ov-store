package ostro.veda.common.error;

public class ErrorHandling {

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

    public static class InvalidStreetAddress extends Exception {
        public InvalidStreetAddress() {
            super("Street Address is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidAddressNumberException extends Exception {
        public InvalidAddressNumberException() {
            super("Address Number is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidAddressType extends Exception {
        public InvalidAddressType() {
            super("Address Type is invalid. Check field requirements and restrictions for valid characters.");
        }
    }

    public static class InvalidPersonName extends Exception {
        public InvalidPersonName() {
            super("Name is invalid. Check field requirements and restrictions for valid characters.");
        }
    }
}
