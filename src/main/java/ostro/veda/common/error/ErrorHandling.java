package main.java.ostro.veda.common.error;

public class ErrorHandling {

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message, String input) {
            super(message + " - [ Input: {" + input + "} ]");
        }
    }
}
