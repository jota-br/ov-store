package test.ostro.veda;

import org.junit.jupiter.api.Test;
import ostro.veda.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void processData() {
        assertNull(UserService.processData("user$123", "", "", "", "", ""));
        assertNull(UserService.processData("short", "", "", "", "", ""));
        assertNull(UserService.processData("user name", "", "", "", "", ""));
        assertNull(UserService.processData("user!name", "", "", "", "", ""));
        assertNull(UserService.processData("*user_123", "", "", "", "", ""));

        assertNotNull(UserService.processData("user_1234", "", "", "", "", ""));
        assertNotNull(UserService.processData("Admin-User", "", "", "", "", ""));
        assertNotNull(UserService.processData("Alpha@Beta", "", "", "", "", ""));
        assertNotNull(UserService.processData("User_42@x", "", "", "", "", ""));
        assertNotNull(UserService.processData("secure-99", "", "", "", "", ""));
    }
}