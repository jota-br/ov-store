package test.ostro.veda;

import org.junit.jupiter.api.Test;
import ostro.veda.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void processData() {
        assertNull(UserService.processData("user$123", "ValidPassword",
                "valid@email.com", "ValidName", "ValidLastName", "5511000000000"));
        assertNull(UserService.processData("validUserName", "invalid#!password78",
                "valid@email.com", "ValidName", "ValidLastName", "5511000000000"));
        assertNull(UserService.processData("validUserName", "ValidPassword",
                "invalidemail.com", "ValidName", "ValidLastName", "5511000000000"));
        assertNull(UserService.processData("validUserName", "ValidPassword",
                "valid@email.com", "Vi", "ValidLastName", "5511000000000"));
        assertNull(UserService.processData("validUserName", "ValidPassword",
                "valid@email.com", "Val", "El", "5511000000000"));
        assertNull(UserService.processData("validUserName", "ValidPassword",
                "valid@email.com", "Val", "Hummer", "55111"));

        assertNotNull(UserService.processData("user_12345", "fdkjrEewe9@w",
                "ema8889_il@google.com", "John", "Vesnki", "5511000000000"));
    }
}