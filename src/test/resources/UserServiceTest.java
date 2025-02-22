package test;

import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.UserRepository;
import ostro.veda.service.UserService;

import static org.junit.Assert.*;

public class UserServiceTest {

    @Test
    public void processData() {
        try (UserRepository userRepository = new UserRepository()) {

            UserService userService = new UserService(userRepository);

            // invalid username
            assertNull(userService.processData(-1,"user$123", "ValidPassword",
                    "valid@email.com", "ValidName", "ValidLastName", "5511000000000", true, ProcessDataType.ADD));

            // invalid password
            assertNull(userService.processData(-1,"validUserName", "short",
                    "valid@email.com", "ValidName", "ValidLastName", "5511000000001", true, ProcessDataType.ADD));
            assertNull(userService.processData(-1,"validUserName", "tooLongMaximumIs20letters",
                    "valid@email.com", "ValidName", "ValidLastName", "5511000000002", true, ProcessDataType.ADD));

            // invalid email
            assertNull(userService.processData(-1,"validUserName", "ValidPassword",
                    "invalidemail.com", "ValidName", "ValidLastName", "5511000000003", true, ProcessDataType.ADD));

            // invalid phone
            assertNull(userService.processData(-1,"validUserName", "ValidPassword",
                    "valid@email.com", "Val", "Hummer", "55111", false, ProcessDataType.ADD));

            String username = "user_123456";
            String password = "fdkjrEewe9@w";
            String email = "ema88890_il@google.com";
            String firstName = "John";
            String lastName = "Vesneski";
            String phone = "5511000000004";

            UserDTO insertedUser = userService.processData(-1, username, password,
                    email, firstName, lastName, phone, true, ProcessDataType.ADD);
            assertNotNull(insertedUser);
            assertEquals(username, insertedUser.getUsername());
            assertEquals(email, insertedUser.getEmail());
            assertEquals(firstName, insertedUser.getFirstName());
            assertEquals(lastName, insertedUser.getLastName());
            assertEquals(phone, insertedUser.getPhone());

            username = "user_New@";
            password = "newPass@";
            email = "newEmail90@hotmail.com";
            firstName = "Johnson";
            lastName = "Iksander";
            phone = "5511999888777";

            UserDTO updatedUser = userService.processData(insertedUser.getUserId(), username, password,
                    email, firstName, lastName, phone, true, ProcessDataType.UPDATE);
            assertNotNull(updatedUser);
            assertEquals(insertedUser.getUserId(), updatedUser.getUserId());
            assertEquals(username, updatedUser.getUsername());
            assertEquals(email, updatedUser.getEmail());
            assertEquals(firstName, updatedUser.getFirstName());
            assertEquals(lastName, updatedUser.getLastName());
            assertEquals(phone, updatedUser.getPhone());
        } catch (Exception ignored) {
        }
    }
}
