package test.resources;

import ostro.veda.service.UserService;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserServiceTest {

    @Test
    public void processData() {
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

        assertNotNull(UserService.processData("user_123456", "fdkjrEewe9@w",
                "ema88890_il@google.com", "John", "Vesnki", "5511000000000"));
    }
}