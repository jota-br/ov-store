package ostro.veda.common;

import ostro.veda.db.helpers.AddressType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    public static String stringChecker(String input, boolean sanitize, boolean spaceAccepted, boolean canBeBull, int minimumLength) {

        String regex = "^[a-zA-Z0-9@_-]+$";
        if (spaceAccepted) {
            regex = "^[\\sa-zA-Z0-9@_-]+$";
        }

        Pattern validPattern = Pattern.compile(regex);
        Matcher matcher = validPattern.matcher(input);

        if (!matcher.matches() || minimumLength > input.length()) {
            if (canBeBull) {
                return "";
            }
            return null;
        }

        if (sanitize) {
            input = stringSanitize(input);
        }

        return input;
    }

    public static String usernameChecker(String input) {
        String regex = "^[a-zA-Z0-9@_-]+$";

        Pattern validPattern = Pattern.compile(regex);
        Matcher matcher = validPattern.matcher(input);

        if (!matcher.matches()) {
            return null;
        }

        return input;
    }

    public static boolean lengthChecker(String input, int min, int max) {
        int length = input.length();
        return length <= max && length >= min;
    }

    public static String stringSanitize(String input) {
//        String characters = "[<>\"'&;/\\\\(){}%|^$]";
        return input
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;")
                .replace("&", "&amp;")
                .replace(";", "\\;")
                .replace("/", "\\/")
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("%", "\\%")
                .replace("|", "\\|")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .trim();
    }

    public static String emailChecker(String input) {
        Pattern validPattern = Pattern.compile("^(?:[a-zA-Z0-9_'^&amp;/+-])+(?:\\.[a-zA-Z0-9_'^&amp;/+-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = validPattern.matcher(input);

        if (!matcher.matches()) {
            return null;
        }

        return input.trim();
    }

    public static String imageUrlCheck(String input) {
        Pattern validPattern = Pattern.compile("^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$");
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

    public static boolean hasValidStreetAddress(String input) {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9\\s,.-/#&]+$");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
    }

    public static boolean hasValidAddressNumber(String input) {
        Pattern validPattern = Pattern.compile("^[0-9A-Za-z\\s#\\-]+$");
        Matcher matcher = validPattern.matcher(input);

        return matcher.matches();
    }

    public static boolean hasValidAddressType(String input) {
        for (AddressType addressType : AddressType.values()) {
            if (addressType.getValue().equals(input)) {
                return true;
            }
        }
        return false;
    }
}
