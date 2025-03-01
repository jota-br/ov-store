package test.ostro.veda.test;

import org.junit.Test;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.UserRepositoryImpl;
import ostro.veda.service.UserServiceImpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class UserServiceImplTest {

    @Test
    public void addUser() {

        ResetTables.resetTables();
        try (
                UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
        ) {
            UserServiceImpl userServiceImpl = new UserServiceImpl(userRepositoryImpl);

            UserDTO userDTO = TestHelper.getUserDTO(userServiceImpl);
            assertNotNull(userDTO);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateUser() {

        ResetTables.resetTables();
        try (
                UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
        ) {
            UserServiceImpl userServiceImpl = new UserServiceImpl(userRepositoryImpl);

            UserDTO userDTO = TestHelper.getUserDTO(userServiceImpl);
            assertNotNull(userDTO);

            UserDTO updatedUserDTO = new UserDTO(userDTO.getUserId(), "newUser90r", null, null,
                    "email2@example.com", "Doe", "John", "+00099988877755",
                    true, userDTO.getRole(), null, null, null, 0);

            userDTO = userServiceImpl.updateUser(updatedUserDTO, "newPassword");
            assertNotNull(userDTO);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}