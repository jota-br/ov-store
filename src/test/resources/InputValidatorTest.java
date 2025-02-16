package test.resources;

import org.junit.Test;
import ostro.veda.common.InputValidator;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.AddressType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class InputValidatorTest {

    @Test
    public void stringChecker() {
    }

    @Test
    public void hasValidUsername() {
        List<String> valid = List.of(
                "Username_123",
                "JohnDoe@123",
                "valid_User",
                "Test-User12",
                "Simple_1234"
        );

        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidUsername(s));
            } catch (ErrorHandling.InvalidUsernameException ignored) {
            }
        }

        List<String> invalid = List.of(
                "Short7",        // Too short
                "ThisIsWayTooLongForThePattern123",  // Too long
                "Invalid!User",  // Contains '!'
                "Spaces notallowed", // Contains spaces
                "User*123"       // Contains '*'
        );

        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidUsername(s));
            } catch (ErrorHandling.InvalidUsernameException ignored) {
            }
        }
    }

    @Test
    public void hasValidPassword() {
        List<String> valid = List.of(
                "Password@123",
                "Valid_Pass!20",
                "Secure#Login8",
                "Example!Pwd_",
                "User@Name20"
        );

        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidPassword(s));
            } catch (ErrorHandling.InvalidPasswordException ignored) {
            }
        }

        List<String> invalid = List.of(
                "Short1!",        // Too short
                "ThisPasswordIsWayTooLong123!",  // Too long
                "Invalid Pass"   // Contains spaces
        );

        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidPassword(s));
            } catch (ErrorHandling.InvalidPasswordException ignored) {
            }
        }
    }

    @Test
    public void hasValidLength() {
        List<String> valid = List.of(
                "Password@123",   // 12 characters
                "ValidPass12",    // 11 characters
                "SecurePwd8",     // 10 characters
                "Username__19",    // 13 characters
                "SingleCharacter" // 15 characters
        );


        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidLength(s, 8, 20));
            } catch (ErrorHandling.InvalidLengthException ignored) {
            }
        }

        List<String> invalid = List.of(
                "Short1",          // 6 characters (too short)
                "WayTooLongPassword12345",  // 24 characters (too long)
                "Tiny",            // 4 characters (too short)
                "Ex@mpl3",        // 7 characters (too short)
                "ThisStringIsWayTooLongToBeValid123"  // 31 characters (too long)
        );


        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidLength(s, 8, 20));
            } catch (ErrorHandling.InvalidLengthException ignored) {
            }
        }
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
        try {
            assertTrue(InputValidator.hasValidName("Furniture"));
        } catch (ErrorHandling.InvalidNameException ignored) {
        }
    }

    @Test
    public void hasValidDescription() {
        List<String> valid = List.of(
                "",
                "Hello, World!",                   // Common phrase with punctuation
                "Valid_Name",                      // Underscores and letters
                "A long sentence that goes on and on, but stays under the limit.", // Sentence with punctuation
                "Simple text.",                    // Basic text with punctuation
                "Complex-Name: Example"            // Hyphens and colons
        );


        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidDescription(s));
            } catch (ErrorHandling.InvalidDescriptionException ignored) {
            }
        }

        List<String> invalid = List.of(
                // 511 characters
                """
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eget sapien lacus. Maecenas vel sagittis diam. Mauris mattis lorem quis urna efficitur, non venenatis turpis egestas. Maecenas mauris magna, ultricies in aliquam nec, gravida at ligula. Curabitur mattis sem id dolor condimentum, eu tempus ligula egestas. Sed a bibendum odio, a suscipit elit. Phasellus in dui eget metus semper ultricies. Mauris sit amet scelerisque magna. Pellentesque neque lacus, molestie a est et, mattis vehicula lorem. Sed aliquet dictum velit, tristique semper lectus volutpat vel.
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris maximus diam dui, ac dapibus massa fermentum et. Vivamus euismod ipsum sed bibendum volutpat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In hac habitasse platea dictumst. Etiam varius posuere facilisis. Ut vulputate ipsum sed neque euismod ultricies. Proin quis urna et lectus rutrum varius sed bibendum velit. Ut non porttitor est. Nullam eget ipsum quis tortor faucibus maximus. Suspendisse mollis, tellus a interdum cursus, purus mi ornare nisi, sit amet lobortis mi erat et dui. Nam condimentum, sem quis luctus consectetur, felis velit sodales erat, sit amet condimentum tellus ipsum quis velit.
                        Vivamus a pharetra quam. Fusce posuere sapien vitae commodo porta. Sed consectetur lacus lorem, in dictum velit tristique vitae. In commodo purus sit amet viverra mollis. Nulla ac nisi pulvinar justo dignissim mollis gravida ut arcu. Etiam id felis ipsum. Donec eget fermentum diam.
                        Donec at leo blandit, commodo ante molestie, interdum elit. Integer at magna ac justo pulvinar feugiat. Sed ultrices sodales quam quis suscipit. In malesuada cursus sagittis. Phasellus in interdum neque. Sed at ullamcorper tortor, vel efficitur lectus. Sed in feugiat felis. Cras tempus mattis pulvinar. Vivamus condimentum in lacus sed tincidunt. Nunc dapibus arcu id lacus ultricies, vitae maximus tortor feugiat. Cras fermentum aliquet ante nec placerat. Nunc viverra, ligula a ornare elementum, magna orci luctus nisl, a tristique sapien elit id diam. In consectetur varius sapien in pellentesque. Donec quis dui ac justo porta tempus vel eu purus. Sed eros neque, dapibus eu quam id, ultricies auctor elit. Vestibulum quis feugiat felis.
                        Phasellus interdum eu lorem non pretium. Proin at tincidunt lacus. Aliquam dictum diam ac velit hendrerit, nec laoreet massa posuere. Proin rhoncus, ante ac consequat imperdiet, sem leo vestibulum nisl, vitae feugiat ligula quam id eros. Aliquam in enim id sem placerat luctus a ut nunc. Donec consequat ante libero, id volutpat lacus finibus vel. Suspendisse iaculis mi dictum velit condimentum vulputate. Fusce tincidunt elementum bibendum. Nam non tincidunt risus, eget dictum sapien. Pellentesque consequat eleifend ex id rhoncus. Maecenas gravida tincidunt mi ac aliquam. Vestibulum vitae aliquam nulla. Pellentesque et pellentesque nisi.
                        Nulla facilisi. Donec tempus ex nec mauris mollis, vitae finibus elit dictum. Sed quis dolor luctus, consequat eros vitae, suscipit nunc. Maecenas pulvinar varius venenatis. Suspendisse ut turpis ligula. Morbi maximus ante nisi, ac iaculis urna tristique in. Vivamus consequat pharetra arcu quis porttitor. Integer malesuada interdum eros, in tincidunt lectus rutrum vel. Sed sit amet commodo sapien."""
        );

        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidDescription(s));
            } catch (ErrorHandling.InvalidDescriptionException ignored) {
            }
        }
    }

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
                assertTrue(InputValidator.hasValidImageUrl(s));
            } catch (ErrorHandling.InvalidImageUrlException ignored) {
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
            try {
                assertFalse(InputValidator.hasValidImageUrl(s));
            } catch (ErrorHandling.InvalidImageUrlException ignored) {
            }
        }
    }

    @Test
    public void encodeUrl() {
        Map<Character, String> map = InputValidator.getEncodeMap();

        for (Map.Entry<Character, String> entry : map.entrySet()) {
            assertEquals(entry.getValue(), InputValidator.encodeUrl(String.valueOf(entry.getKey())));
        }

        Map<String, String> urlMap = getUrlMap();


        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            assertEquals(entry.getValue(), InputValidator.encodeUrl(String.valueOf(entry.getKey())));
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

    @Test
    public void hasValidEmail() {
        List<String> valid = List.of(
                "user@example.com",
                "user.name@example.co.uk",
                "user_name123@example.org",
                "user+name@example-domain.com",
                "user-name@example.museum"
        );

        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidEmail(s));
            } catch (ErrorHandling.InvalidEmailException ignored) {
            }
        }

        List<String> invalid = List.of(
                "user@.com",                  // Missing domain
                "user@domain..com",           // Double dot in domain
                "user@domain.c",              // TLD too short
                "user@domain.toolongtldtoolongtldtoolongtldtoolongtldtoolongtldtoolongtldtoolongtld.com",  // TLD too long
                "@example.com"                // Missing local part
        );

        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidImageUrl(s));
            } catch (ErrorHandling.InvalidImageUrlException ignored) {
            }
        }
    }

    @Test
    public void hasValidPhone() {
        List<String> valid = List.of(
                "+123456",         // Minimum length
                "+1234567890",     // Common length
                "+12345678901234", // Maximum length
                "+987654321",      // Another valid example
                "+1234567890123"   // Near maximum length
        );

        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidPhone(s));
            } catch (ErrorHandling.InvalidPhoneException ignored) {
            }
        }

        List<String> invalid = List.of(
                "123456",         // Missing the leading '+'
                "+12345",         // Too short
                "+123456789012345", // Too long
                "+12a45678",      // Contains a non-digit character
                "+ 12345678"      // Contains a space
        );


        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidPhone(s));
            } catch (ErrorHandling.InvalidPhoneException ignored) {
            }
        }
    }

    @Test
    public void hasValidStreetAddress() {
        List<String> valid = List.of(
                "Hello World",
                "1234 Main St.",
                "Product#1234",
                "Simple-Test",
                "Business & Co.",
                "12345",
                "This is a longer text, but it should still be valid within the 255 character limit. It includes various characters like - , . / and even &."
        );


        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidStreetAddress(s));
            } catch (ErrorHandling.InvalidStreetAddress ignored) {
            }
        }

        List<String> invalid = List.of(
                "",
                "This string contains a character not allowed: @",
                "Special*Character",
                "Too!Many@Symbols",
                """
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean interdum ultricies pulvinar. Donec tempor quam id lacus tincidunt, eu tristique purus egestas. Curabitur erat sapien, feugiat a leo sit amet, auctor ornare tortor. Curabitur ac hendrerit mi, sit amet pellentesque nulla. Sed nec feugiat sapien. Suspendisse varius lectus et semper placerat. Donec sem ipsum, blandit vitae tincidunt nec, elementum sit amet metus.
                        Donec sed nunc et orci sodales sollicitudin. Quisque quis scelerisque nunc. Nunc ornare posuere ultricies. Fusce sit amet finibus neque. Integer sed turpis tortor. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nulla est odio, imperdiet vel mi eget, malesuada mattis elit. Maecenas in cursus mi. Vivamus efficitur auctor nisl nec elementum. Praesent orci ipsum, auctor in orci sed, interdum facilisis sem. Donec molestie sed sapien nec tincidunt. Cras a erat lacus. In vestibulum commodo erat, eu blandit diam tempus at. Phasellus ultrices scelerisque molestie. Ut eu enim magna. In ut odio vel justo tincidunt mollis.
                        Integer dapibus dui sed semper ornare. In volutpat lobortis nulla eget vehicula. Morbi pellentesque sapien eget odio scelerisque feugiat. Nunc vel suscipit mi, facilisis molestie purus. Nam hendrerit libero a facilisis cursus. Aenean diam sem, placerat ut consectetur id, suscipit a sapien. Etiam nisi massa, venenatis quis quam ut, efficitur feugiat diam. Maecenas quis congue lectus. In at tristique leo. Aliquam venenatis lacus condimentum feugiat ultricies. Curabitur hendrerit libero ante, at tincidunt massa lacinia nec. Proin mollis felis felis, eget rhoncus ligula convallis nec. Praesent at mauris ornare, pulvinar mi ut, auctor risus. Maecenas in feugiat."""
        );

        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidStreetAddress(s));
            } catch (ErrorHandling.InvalidStreetAddress ignored) {
            }
        }
    }

    @Test
    public void hasValidAddressNumber() {
        List<String> valid = List.of(
                "1234",
                "5678A",
                "1234-B",
                "Apt 123",
                "Suite #56",
                "42B-67",
                "4567/12",
                "Bldg #8"
        );

        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidAddressNumber(s));
            } catch (ErrorHandling.InvalidAddressNumberException ignored) {
            }
        }

        List<String> invalid = List.of(
                "",
                "123*",
                "Suite 123@",
                "Apartment$45",
                "Street!10",
                "Invalid_Address%",
                "LoremipsumdolorsitametconsecteturvestibulumLoremipsumdolorsitametconsecteturvestibulum"
        );

        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidAddressNumber(s));
            } catch (ErrorHandling.InvalidAddressNumberException ignored) {
            }
        }
    }

    @Test
    public void hasValidAddressType() {
        List<AddressType> valid = List.of(
                AddressType.HOME,
                AddressType.WORK,
                AddressType.BILLING,
                AddressType.SHIPPING,
                AddressType.DELIVERY,
                AddressType.PICKUP,
                AddressType.OFFICE,
                AddressType.STORE,
                AddressType.PO_BOX,
                AddressType.WAREHOUSE,
                AddressType.OTHER
        );

        for (AddressType at : valid) {
            try {
                assertTrue(InputValidator.hasValidAddressType(at.getValue()));
            } catch (ErrorHandling.InvalidAddressType ignored) {
            }
        }
    }

    @Test
    public void hasValidPersonName() {
        List<String> valid = List.of(
                "John Doe",
                "Élise Müller",
                "José Rodríguez",
                "Renée O'Connor",
                "Märta Löfgren",
                "Björn Borg",
                "Marie Curie",
                "Éric Zemmour",
                "André Dupont",
                "François L'Overture"
        );

        for (String s : valid) {
            try {
                assertTrue(InputValidator.hasValidPersonName(s));
            } catch (ErrorHandling.InvalidPersonName ignored) {
            }
        }

        List<String> invalid = List.of(
                "John@Doe",
                "Elise_Müller",
                "José#Rodríguez",
                "Renée$O'Connor",
                "Märta*Löfgren",
                "Björn!Borg",
                "Marie Curie123",
                "Éric Zemmour%",
                "AddressWithMoreThan255Characters..................................................................."
                        + "................................................................................................"
                        + "................................................"
        );

        for (String s : invalid) {
            try {
                assertFalse(InputValidator.hasValidPersonName(s));
            } catch (ErrorHandling.InvalidPersonName ignored) {
            }
        }
    }
}