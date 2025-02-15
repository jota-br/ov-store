package ostro.veda.common;

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

    public static boolean hasValidUsername(String input) {
        Pattern validPattern = Pattern.compile("^[a-zA-Z0-9@_-]{8,20}$");
        Matcher matcher = validPattern.matcher(input);

        return matcher.matches();
    }

    public static boolean hasValidPassword(String input) {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9!@#\\$%\\^&\\*\\(\\)_\\+\\-\\=\\{\\}\\[\\]:;\"'<>\\,\\.?/|\\\\]{8,20}$");
        Matcher matcher = validPattern.matcher(input);

        return matcher.matches();
    }

    public static boolean hasValidLength(String input, int min, int max) {
        int length = input.length();
        return length <= max && length >= min;
    }

    public static String stringSanitize(String input) {
//        String characters = "[<>\"'&;/\\\\(){}%|^$]";
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

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(sanitize.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    public static boolean hasValidName(String input) {
        Pattern validPattern = Pattern.compile("^[\\sA-Za-z\\p{Punct}]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
    }

    public static boolean hasValidDescription(String input) {
        Pattern validPattern = Pattern.compile("^[a-zA-Z\\s\\p{Punct}]{0,510}$");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
    }

    public static boolean hasValidUrl(String input) {
        Pattern validPattern = Pattern.compile("^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
    }

    public static String encodeUrl(String input) {
        Map<String, String> encodingMap = new HashMap<>();
        encodingMap.put(" ", "%20");
        encodingMap.put("!", "%21");
        encodingMap.put("\"", "%22");
        encodingMap.put("#", "%23");
        encodingMap.put("$", "%24");
        encodingMap.put("%", "%25");
        encodingMap.put("&", "%26");
        encodingMap.put("'", "%27");
        encodingMap.put("(", "%28");
        encodingMap.put(")", "%29");
        encodingMap.put("*", "%2A");
        encodingMap.put("+", "%2B");
        encodingMap.put(",", "%2C");
        encodingMap.put("-", "%2D");
        encodingMap.put(".", "%2E");
        encodingMap.put("/", "%2F");
        encodingMap.put(":", "%3A");
        encodingMap.put(";", "%3B");
        encodingMap.put("<", "%3C");
        encodingMap.put("=", "%3D");
        encodingMap.put(">", "%3E");
        encodingMap.put("?", "%3F");
        encodingMap.put("@", "%40");
        encodingMap.put("[", "%5B");
        encodingMap.put("\\", "%5C");
        encodingMap.put("]", "%5D");
        encodingMap.put("^", "%5E");
        encodingMap.put("_", "%5F");
        encodingMap.put("`", "%60");
        encodingMap.put("{", "%7B");
        encodingMap.put("|", "%7C");
        encodingMap.put("}", "%7D");
        encodingMap.put("~", "%7E");

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(encodingMap.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    public static boolean hasValidEmail(String input) {
        Pattern validPattern = Pattern.compile("^(?:[a-zA-Z0-9_'^&amp;/+-])+(?:\\.[a-zA-Z0-9_'^&amp;/+-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,255}$");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
    }

    public static boolean hasValidPhone(String input) {
        Pattern validPattern = Pattern.compile("\\+\\d{6,14}");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
    }

    public static boolean hasValidStreetAddress(String input) {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9\\s,.\\-/#&]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
    }

    public static boolean hasValidAddressNumber(String input) {
        Pattern validPattern = Pattern.compile("^[0-9A-Za-z\\s#\\-]{1,50}$");
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

    public static boolean hasValidPersonName(String input) {
        Pattern validPattern = Pattern.compile("^[A-Za-zéüñçöå\\-'\\s]{0,255}$");
        Matcher matcher = validPattern.matcher(input);
        return matcher.matches();
//        Map<Character, Boolean> validCharacters = new HashMap<>();
//        validCharacters.put('A', true);
//        validCharacters.put('B', true);
//        validCharacters.put('C', true);
//        validCharacters.put('D', true);
//        validCharacters.put('E', true);
//        validCharacters.put('F', true);
//        validCharacters.put('G', true);
//        validCharacters.put('H', true);
//        validCharacters.put('I', true);
//        validCharacters.put('J', true);
//        validCharacters.put('K', true);
//        validCharacters.put('L', true);
//        validCharacters.put('M', true);
//        validCharacters.put('N', true);
//        validCharacters.put('O', true);
//        validCharacters.put('P', true);
//        validCharacters.put('Q', true);
//        validCharacters.put('R', true);
//        validCharacters.put('S', true);
//        validCharacters.put('T', true);
//        validCharacters.put('U', true);
//        validCharacters.put('V', true);
//        validCharacters.put('W', true);
//        validCharacters.put('X', true);
//        validCharacters.put('Y', true);
//        validCharacters.put('Z', true);
//        validCharacters.put('a', true);
//        validCharacters.put('b', true);
//        validCharacters.put('c', true);
//        validCharacters.put('d', true);
//        validCharacters.put('e', true);
//        validCharacters.put('f', true);
//        validCharacters.put('g', true);
//        validCharacters.put('h', true);
//        validCharacters.put('i', true);
//        validCharacters.put('j', true);
//        validCharacters.put('k', true);
//        validCharacters.put('l', true);
//        validCharacters.put('m', true);
//        validCharacters.put('n', true);
//        validCharacters.put('o', true);
//        validCharacters.put('p', true);
//        validCharacters.put('q', true);
//        validCharacters.put('r', true);
//        validCharacters.put('s', true);
//        validCharacters.put('t', true);
//        validCharacters.put('u', true);
//        validCharacters.put('v', true);
//        validCharacters.put('w', true);
//        validCharacters.put('x', true);
//        validCharacters.put('y', true);
//        validCharacters.put('z', true);
//        validCharacters.put('é', true);
//        validCharacters.put('ü', true);
//        validCharacters.put('ñ', true);
//        validCharacters.put('ç', true);
//        validCharacters.put('ö', true);
//        validCharacters.put('å', true);
//        validCharacters.put('-', true);
//        validCharacters.put('\'', true);
//        validCharacters.put(' ', true);
//
//        for (char c : input.toCharArray()) {
//            if (!validCharacters.containsKey(c)) return false;
//        }
//        return true;
    }
}
