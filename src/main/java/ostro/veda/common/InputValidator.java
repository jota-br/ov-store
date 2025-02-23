package ostro.veda.common;

import org.apache.commons.validator.routines.EmailValidator;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.AddressType;

import java.util.HashMap;
import java.util.Map;
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

    public static boolean hasValidUsername(String input) throws ErrorHandling.InvalidUsernameException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z0-9@_-]{8,20}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidUsernameException();
    }

    public static boolean hasValidPassword(String input) throws ErrorHandling.InvalidPasswordException {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/|\\\\]{8,20}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidPasswordException();
    }

    public static boolean hasValidLength(String input, int min, int max) throws ErrorHandling.InvalidLengthException {
        int length = input.length();
        if (length <= max && length >= min) {
            return true;
        }
        throw new ErrorHandling.InvalidLengthException();
    }

    public static String stringSanitize(String input) {
//        String characters = "[<>\"'&;/\\\\(){}%|^$]";
        Map<Character, String> sanitize = getSanitizeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(sanitize.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    private static Map<Character, String> getSanitizeMap() {
        Map<Character, String> sanitize = new HashMap<>();
        sanitize.put('\'', "&apos;");
        sanitize.put('&', "&amp;");
        sanitize.put(';', "\\;");
        sanitize.put('/', "\\/");
        sanitize.put('\\', "\\\\");
        sanitize.put('(', "\\(");
        sanitize.put(')', "\\)");
        sanitize.put('{', "\\{");
        sanitize.put('}', "\\}");
        sanitize.put('<', "&lt;");
        sanitize.put('>', "&gt;");
        sanitize.put('\"', "&quot;");
        sanitize.put('%', "\\%");
        sanitize.put('|', "\\|");
        sanitize.put('^', "\\^");
        sanitize.put('$', "\\$");
        return sanitize;
    }

    public static boolean hasValidName(String input) throws ErrorHandling.InvalidNameException {
        Pattern validPattern = Pattern.compile("^[\\sA-Za-z\\p{Punct}]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidNameException();
    }

    public static boolean hasValidDescription(String input) throws ErrorHandling.InvalidDescriptionException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z\\s\\p{Punct}\n]{0,510}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidDescriptionException();
    }

    public static boolean hasValidEmail(String input) throws ErrorHandling.InvalidEmailException {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (emailValidator.isValid(input)) {
            return true;
        }
        throw new ErrorHandling.InvalidEmailException();
    }

    public static boolean hasValidPhone(String input) throws ErrorHandling.InvalidPhoneException {
        Pattern validPattern = Pattern.compile("\\+\\d{6,14}");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidPhoneException();
    }

    public static boolean hasValidStreetAddress(String input) throws ErrorHandling.InvalidStreetAddressException {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9\\s,.\\-/#&]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidStreetAddressException();
    }

    public static boolean hasValidAddressNumber(String input) throws ErrorHandling.InvalidAddressNumberException {
        Pattern validPattern = Pattern.compile("^[0-9A-Za-z\\s#\\-/]{1,50}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidAddressNumberException();
    }

    public static boolean hasValidAddressType(String input) throws ErrorHandling.InvalidAddressTypeException {
        for (AddressType addressType : AddressType.values()) {
            if (addressType.getValue().equals(input)) {
                return true;
            }
        }
        throw new ErrorHandling.InvalidAddressTypeException();
    }

    public static boolean hasValidPersonName(String input) throws ErrorHandling.InvalidPersonNameException {
        Pattern validPattern = Pattern.compile("^[A-Za-záéíóúüñçöäåèàìòùêâîôûëïÿÁÉÍÓÚÜÑÇÖÄÅÈÀÌÒÙÊÂÎÔÛËÏŸ\\-'\\s]{0,255}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidPersonNameException();
    }
}
