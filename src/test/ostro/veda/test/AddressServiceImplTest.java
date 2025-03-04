package test.ostro.veda.test;

import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.RoleDTO;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.config.AppConfig;
import ostro.veda.service.AddressServiceImpl;
import ostro.veda.service.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class AddressServiceImplTest {

    private UserDTO getUserDTO() {

        RoleDTO roleDTO = new RoleDTO(20, null, null,
                null, null, null, 0);

        AddressDTO addressDTO = new AddressDTO(0, null, "Street 234 West",
                "123-B", "Home", "Some City Name", "State of Where",
                "90222000", "The United Country", true, null, null, 0);

        UserDTO userDTO = new UserDTO(0, "username93", null, null,
                "email@example.com", "John", "Doe", "+5511122233344",
                true, roleDTO, List.of(), null, null, 0);

        addressDTO.setUser(userDTO);
        return userDTO;
    }

    private AddressDTO getAddressDTO(UserDTO userDTO) {

        AddressDTO addressDTO = new AddressDTO(0, userDTO, "Street 234 West",
                "123-B", "Home", "Some City Name", "State of Where",
                "90222000", "The United Country", true, null, null, 0);

        return addressDTO;
    }

    private AddressDTO getAddressDTOWithId(UserDTO userDTO, int id) {

        return new AddressDTO(id, userDTO, "Avenue Park Hill",
                "#394", "Work", "Johnsonville", "Phils",
                "73488123", "United Kingdom", true, null, null, 0);
    }

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        AddressServiceImpl addressService = context.getBean(AddressServiceImpl.class);

        UserDTO userDTO = userService.add(getUserDTO(), "password");
        AddressDTO addressDTO = addressService.add(getAddressDTO(userDTO));
        assertNotNull(addressDTO);

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        AddressServiceImpl addressService = context.getBean(AddressServiceImpl.class);

        UserDTO userDTO = userService.add(getUserDTO(), "password");
        AddressDTO addressDTO = addressService.add(getAddressDTO(userDTO));
        addressDTO = addressService.update(getAddressDTOWithId(userDTO, addressDTO.getAddressId()));
        assertNotNull(addressDTO);

        context.close();

    }
}