package ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.config.AppConfig;
import ostro.veda.model.dto.UserDto;
import ostro.veda.service.UserServiceImpl;

import static org.junit.Assert.assertNotNull;

public class UserServiceImplTest {

    private static Helper helper = new Helper();

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);

        UserDto userDto = userService.add(helper.getUserDTO(), "password90*&");
        assertNotNull(userDto);

        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);

        UserDto userDTO = userService.add(helper.getUserDTO(), "password90@&");
        userDTO = userService.update(helper.getUserDTOWithId(userDTO.getUserId()), "password90*");
        assertNotNull(userDTO);

        context.close();

    }
}