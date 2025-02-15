package test.resources;

import org.junit.Test;
import ostro.veda.common.InputValidator;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InputValidatorTest {

    @Test
    public void stringChecker() {
    }

    @Test
    public void hasValidUsername() {
    }

    @Test
    public void hasValidPassword() {
    }

    @Test
    public void hasValidLength() {
    }

    @Test
    public void stringSanitize() {
        Map<Character, String> map = getCharacterStringMap();

        for (Map.Entry<Character, String> entry : map.entrySet()) {
            assertEquals(entry.getValue(), InputValidator.stringSanitize(String.valueOf(entry.getKey())));
        }
    }

    private static Map<Character, String> getCharacterStringMap() {
        Map<Character, String> map = new HashMap<>();
        map.put('\'', "&apos;");
        map.put('&', "&amp;");
        map.put(';', "\\;");
        map.put('/', "\\/");
        map.put('\\', "\\\\");
        map.put('(', "\\(");
        map.put(')', "\\)");
        map.put('{', "\\{");
        map.put('}', "\\}");
        map.put('<', "&lt;");
        map.put('>', "&gt;");
        map.put('\"', "&quot;");
        map.put('%', "\\%");
        map.put('|', "\\|");
        map.put('^', "\\^");
        map.put('$', "\\$");
        return map;
    }

    @Test
    public void hasValidName() {
        assertTrue(InputValidator.hasValidName("Furniture"));
    }

    @Test
    public void hasValidDescription() {
    }

    @Test
    public void hasValidUrl() {
    }

    @Test
    public void encodeUrl() {
    }

    @Test
    public void hasValidEmail() {
    }

    @Test
    public void hasValidPhone() {
    }

    @Test
    public void hasValidStreetAddress() {
    }

    @Test
    public void hasValidAddressNumber() {
    }

    @Test
    public void hasValidAddressType() {
    }

    @Test
    public void hasValidPersonName() {
    }
}