package ostro.veda;

import ostro.veda.dto.UserDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    public static UserDTO processData(String username, String password, String email,
                                      String firstName, String lastName, String phone) {

        int usernameMinLength = 8;
        // acceptable characters @ _ - [a-zA-Z0-9]
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9@_-]+$");
        Matcher usernameMatcher = usernamePattern.matcher(username);

        if (!usernameMatcher.matches() || username.length() < usernameMinLength) {
            return null;
        }

        return new UserDTO(1, "", "", "", "", "", "", "", true);
    }

    public static void main(String[] args) {
        String valid = "user_1234";
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9@_-]+$");
        Matcher usernameMatcher = usernamePattern.matcher(valid);

        if (usernameMatcher.matches()) {
            System.out.println("true");
        }
    }
}
