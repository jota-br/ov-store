package ostro.veda.util.sanitization;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Validated
public interface SanitizeFunction<T, R> {

    R sanitize(@NotNull T t);

    default String sanitizeString(String input) {

        Map<Character, String> sanitizeMap = getSanitizeMap();

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(sanitizeMap.getOrDefault(c, String.valueOf(c)));
        }

        return sb.toString();

    }

    private Map<Character, String> getSanitizeMap() {

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
