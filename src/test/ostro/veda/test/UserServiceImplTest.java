package test.ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.RoleDTO;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.config.AppConfig;
import ostro.veda.service.UserServiceImpl;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class UserServiceImplTest {

    private UserDTO getUserDTO() {

        RoleDTO roleDTO = new RoleDTO(20, null, null,
                null, null, null, 0);

        AddressDTO addressDTO = new AddressDTO(0, null, "Street 234 West",
                "123-B", "Home", "Some City Name", "State of Where",
                "90222000", "The United Country", true, null, null, 0);

        UserDTO userDTO = new UserDTO(0, "username92", null, null,
                "email@example.com", "John", "Doe", "+5511122233344",
                true, roleDTO, List.of(addressDTO), null, null, 0);

        addressDTO.setUser(userDTO);
        return userDTO;
    }

    private UserDTO getUserDTOWithId(int id) {

        RoleDTO roleDTO = new RoleDTO(15, null, null,
                null, null, null, 0);

        AddressDTO addressDTO = new AddressDTO(0, null, "Street 234 West",
                "123-B", "Home", "Some City Name", "State of Where",
                "90222000", "The United Country", true, null, null, 0);

        UserDTO userDTO = new UserDTO(id, "username@93", null, null,
                "anotheremail@example.com", "Doe", "John", "+5599988877766",
                true, roleDTO, List.of(), null, null, 0);

        addressDTO.setUser(userDTO);
        return userDTO;
    }

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);

        UserDTO userDTO = userService.add(getUserDTO(), "password90*&");
        assertNotNull(userDTO);

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);

        UserDTO userDTO = userService.add(getUserDTO(), "password90*&");
        userDTO = userService.update(getUserDTOWithId(userDTO.getUserId()), "password90");
        assertNotNull(userDTO);

        context.close();

    }
}