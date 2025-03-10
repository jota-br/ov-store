package ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.UserDto;
import ostro.veda.config.AppConfig;
import ostro.veda.service.AddressServiceImpl;
import ostro.veda.service.UserServiceImpl;

import static org.junit.Assert.assertNotNull;

public class AddressServiceImplTest {

    private static Helper helper = new Helper();

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        AddressServiceImpl addressService = context.getBean(AddressServiceImpl.class);

        UserDto userDTO = userService.add(helper.getUserDTO(), "password");
        AddressDto addressDTO = addressService.add(helper.getAddressDTO(userDTO));
        assertNotNull(addressDTO);

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        AddressServiceImpl addressService = context.getBean(AddressServiceImpl.class);

        UserDto userDTO = userService.add(helper.getUserDTO(), "password");
        AddressDto addressDTO = addressService.add(helper.getAddressDTO(userDTO));
        addressDTO = addressService.update(helper.getAddressDTOWithId(userDTO, addressDTO.getAddressId()));
        assertNotNull(addressDTO);

        context.close();

    }
}