package ostro.veda.common.validation;

import java.util.HashMap;
import java.util.Map;

public class UrlEncoder {

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
}
