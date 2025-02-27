package test.resources;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UrlEncoderTest {

    @Test
    public void encodeUrl() {
        Map<Character, String> map = UrlEncoder.getEncodeMap();

        for (Map.Entry<Character, String> entry : map.entrySet()) {
            assertEquals(entry.getValue(), UrlEncoder.encodeUrl(String.valueOf(entry.getKey())));
        }

        Map<String, String> urlMap = getUrlMap();
        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            assertEquals(entry.getValue(), UrlEncoder.encodeUrl(String.valueOf(entry.getKey())));
        }
    }

    private static Map<String, String> getUrlMap() {
        Map<String, String> urlMap = new HashMap<>();

        urlMap.put("https://example.com/search?q=Hello World!", "https://example.com/search?q=Hello%20World%21");
        urlMap.put("http://example.com/test?name=John&age=30", "http://example.com/test?name=John%26age=30");
        urlMap.put("https://www.example.com/images/image.png", "https://www.example.com/images/image.png");
        urlMap.put("http://example.com/path/to/resource?key=value#anchor", "http://example.com/path/to/resource?key=value%23anchor");
        urlMap.put("https://sub.domain.example.com/page?param1=value1&param2=value2", "https://sub.domain.example.com/page?param1=value1%26param2=value2");
        return urlMap;
    }
}