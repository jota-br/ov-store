package main.java.ostro.veda.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    public static String stringChecker(String input, boolean sanitize, int minimumLength) {
        Pattern validPattern = Pattern.compile("^[a-zA-Z0-9@_-]+$");
        Matcher matcher = validPattern.matcher(input);

        if (!matcher.matches() || minimumLength > input.length()) {
            return null;
        }

        if (sanitize) {
            input = stringSanitize(input);
        }

        return input;
    }

    private static String stringSanitize(String input) {
        return input.replaceAll("[<>\"'&;/\\\\(){}%|^$]", "").trim();
    }

    public static String emailChecker(String input) {
        Pattern validPattern = Pattern.compile("^(?:[a-zA-Z0-9_'^&amp;/+-])+(?:\\.[a-zA-Z0-9_'^&amp;/+-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = validPattern.matcher(input);

        if (!matcher.matches()) {
            return null;
        }

        return input.trim();
    }

    public static String phoneChecker(String input) {
        Pattern validPattern = Pattern.compile("\\+\\d{6,14}");
        Matcher matcher = validPattern.matcher(input);

        if (!matcher.matches()) {
            return null;
        }

        return input;
    }
}
