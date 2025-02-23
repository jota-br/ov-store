package test.resources;

import org.junit.Test;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.ProductValidation;

import java.util.List;

import static org.junit.Assert.*;

public class ProductValidationTest {

    @Test
    public void hasValidImageUrl() {
        List<String> valid = List.of(
                "https://example.com/image.png",
                "http://subdomain.example.com/path/to/image.png",
                "example.com/image.png",
                "https://example.com/image.png?version=1.0",
                "http://example.com/path/to/image/file-name.png"
        );

        for (String s : valid) {
            try {
                assertTrue(ProductValidation.hasValidImageUrl(s));
            } catch (ErrorHandling.InvalidInputException e) {
                fail(e.getMessage());
            }
        }

        List<String> invalid = List.of(
                "https://example.com/image.jpg",  // Invalid file extension
                "ftp://example.com/image.png",    // Invalid protocol (ftp instead of http/https)
                "example.com/image",              // Missing .png extension
                "http://example.com/image.png/",  // Trailing slash not followed by parameters
                "https://example.com/path/to/image" // Missing .png extension
        );

        for (String s : invalid) {
            assertThrows(ErrorHandling.InvalidInputException.class, () -> ProductValidation.hasValidImageUrl(s));
        }
    }
}
