package test.resources;

import org.junit.Test;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.UserRepository;
import ostro.veda.service.UserService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class UserServiceTest {

    @Test
    public void addUser() {

        ResetTables.resetTables();
        try (
                UserRepository userRepository = new UserRepository();
        ) {
            UserService userService = new UserService(userRepository);

            UserDTO userDTO = TestHelper.getUserDTO(userService);
            assertNotNull(userDTO);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateUser() {

        ResetTables.resetTables();
        try (
                UserRepository userRepository = new UserRepository();
        ) {
            UserService userService = new UserService(userRepository);

            UserDTO userDTO = TestHelper.getUserDTO(userService);
            assertNotNull(userDTO);

            UserDTO updatedUserDTO = new UserDTO(userDTO.getUserId(), "newUser90r", null, null,
                    "email2@example.com", "Doe", "John", "+00099988877755",
                    true, userDTO.getRole(), null, null, null);

            userDTO = userService.updateUser(updatedUserDTO, "newPassword");
            assertNotNull(userDTO);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}