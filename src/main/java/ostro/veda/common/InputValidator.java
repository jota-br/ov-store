package ostro.veda.common;

import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.AddressType;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Product;

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

    public static boolean hasValidImageUrl(String input) throws ErrorHandling.InvalidImageUrlException {
        Pattern validPattern = Pattern.compile("^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidImageUrlException();
    }

    public static String encodeUrl(String input) {
        Map<Character, String> encodingMap = getEncodeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(encodingMap.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    public static Map<Character, String> getEncodeMap() {
        Map<Character, String> encodingMap = new HashMap<>();
        encodingMap.put(' ', "%20");
        encodingMap.put('!', "%21");
        encodingMap.put('\"', "%22");
        encodingMap.put('#', "%23");
        encodingMap.put('$', "%24");
        encodingMap.put('%', "%25");
        encodingMap.put('&', "%26");
        encodingMap.put('\'', "%27");
        encodingMap.put('(', "%28");
        encodingMap.put(')', "%29");
        encodingMap.put('*', "%2A");
        encodingMap.put('+', "%2B");
        encodingMap.put(',', "%2C");
        encodingMap.put('-', "%2D");
//        encodingMap.put('.', "%2E");
        encodingMap.put(';', "%3B");
        encodingMap.put('<', "%3C");
//        encodingMap.put('=', "%3D");
        encodingMap.put('>', "%3E");
//        encodingMap.put('?', "%3F");
        encodingMap.put('@', "%40");
        encodingMap.put('[', "%5B");
        encodingMap.put('\\', "%5C");
        encodingMap.put(']', "%5D");
        encodingMap.put('^', "%5E");
        encodingMap.put('_', "%5F");
        encodingMap.put('`', "%60");
        encodingMap.put('{', "%7B");
        encodingMap.put('|', "%7C");
        encodingMap.put('}', "%7D");
        encodingMap.put('~', "%7E");
        // encodingMap.put('/', "%2F");
        // encodingMap.put(':', "%3A");
        return encodingMap;
    }

    public static boolean hasValidEmail(String input) throws ErrorHandling.InvalidEmailException {
        Pattern validPattern = Pattern.compile("^(?=.{2,255}$)(?:[a-zA-Z0-9_'^&amp;/+-])+(?:\\.[a-zA-Z0-9_'^&amp;/+-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
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

    public static boolean hasValidValue(double input) throws ErrorHandling.InvalidValueException {
        if (input >= 0.0) {
            return true;
        }
        throw new ErrorHandling.InvalidValueException();
    }

    public static boolean hasValidOrderStatus(OrderStatus status)
            throws ErrorHandling.InvalidOrderStatusException {
        if (status != null) {
            return true;
        }
        throw new ErrorHandling.InvalidOrderStatusException();
    }

    public static boolean hasValidAddress(int userId, Address address) throws ErrorHandling.InvalidAddressException {
        if (address != null && address.getUserId() == userId) {
            return true;
        }
        throw new ErrorHandling.InvalidAddressException();
    }

    public static boolean hasValidProductAndQuantity(Map<Product, Integer> productAndQuantity)
            throws ErrorHandling.InvalidProductException, ErrorHandling.InvalidQuantityException {
        for (Map.Entry<Product, Integer> entry : productAndQuantity.entrySet()) {
            if (entry.getKey() == null) {
                throw new ErrorHandling.InvalidProductException();
            } else if (entry.getValue() < 0) {
                throw new ErrorHandling.InvalidQuantityException();
            }
        }
        return true;
    }

    public static boolean hasValidProcessDataType(ProcessDataType input, ProcessDataType required)
            throws ErrorHandling.InvalidProcessDataType {
        if (input == null || !input.getType().equals(required.getType())) {
            throw new ErrorHandling.InvalidProcessDataType();
        }
        return true;
    }
}
