package test.resources;

import ostro.veda.common.ProcessDataType;
import ostro.veda.db.UserRepository;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.service.UserService;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserServiceTest {

    @Test
    public void processData() {
        EntityManagerHelper entityManagerHelper = new EntityManagerHelper();
        try (UserRepository userRepository = new UserRepository(entityManagerHelper)) {

            UserService userService = new UserService(userRepository);

            // invalid username
            assertNull(userService.processData("user$123", "ValidPassword",
                    "valid@email.com", "ValidName", "ValidLastName", "5511000000000", true, ProcessDataType.ADD));

            // invalid password
            assertNull(userService.processData("validUserName", "invalid#!password78",
                    "valid@email.com", "ValidName", "ValidLastName", "5511000000000", true, ProcessDataType.ADD));

            // invalid email
            assertNull(userService.processData("validUserName", "ValidPassword",
                    "invalidemail.com", "ValidName", "ValidLastName", "5511000000000", true, ProcessDataType.ADD));

            // invalid phone
            assertNull(userService.processData("validUserName", "ValidPassword",
                    "valid@email.com", "Val", "Hummer", "55111", false, ProcessDataType.ADD));

            assertNotNull(userService.processData("user_123456", "fdkjrEewe9@w",
                    "ema88890_il@google.com", "John", "Vesnki", "5511000000000", true, ProcessDataType.ADD));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}