package test.ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.config.AppConfig;
import ostro.veda.service.AddressServiceImpl;

public class AddressRepositoryImplTest {

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//        UserDTO userDTO = TestHelper.getUserDTO(new UserService(new UserRepository()));
        context.getBean(AddressServiceImpl.class).add(new AddressDTO(0, 1, "Street N123", "1900-B",
                "Home", "Hollville", "State of Play", "900103041", "Brazil", true, null, null, 0));

        context.close();
    }
}