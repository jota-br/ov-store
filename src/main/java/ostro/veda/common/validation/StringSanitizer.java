package ostro.veda.common.validation;

import java.util.HashMap;
import java.util.Map;

public class StringSanitizer {

    /**
     * Sanitize String using getSanitizeMap.
     * @param input String.
     * @return Sanitized String.
     */
    public static String sanitize(String input) {
        Map<Character, String> sanitize = getSanitizeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(sanitize.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    /**
     * Sanitize Characters Map.
     * @return Map with to be sanitized Character and clean String.
     */
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
}
