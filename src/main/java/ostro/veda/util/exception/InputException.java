package ostro.veda.util.exception;

public class InputException {

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message, String input) {
            super(message + " - [ Input: {" + input + "} ]");
        }
    }
}
