package test.resources;

import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.db.AddressRepository;
import ostro.veda.db.UserRepository;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.service.AddressService;
import ostro.veda.service.UserService;

import static org.junit.Assert.*;

public class AddressServiceTest {

    @Test
    public void processData() {

        EntityManagerHelper entityManagerHelper = new EntityManagerHelper();
        try (AddressRepository addressRepository = new AddressRepository(entityManagerHelper)) {
            createUser(entityManagerHelper);
            AddressService addressService = new AddressService(addressRepository);

            int id = -1;
            String street = "XV de Novembro";
            String addressNumber = "10900";
            String addressType = "home";
            String city = "Joinville";
            String state = "Santa Catarina";
            String zipCode = "00000000";
            String country = "Brazil";


            AddressDTO addressDTO = addressService.processData(-1, street, addressNumber,
                    addressType, city, state, zipCode, country, true, ProcessDataType.ADD);

            assertEquals(street, addressDTO.getStreetAddress());
            assertEquals(addressType, addressDTO.getAddressType());
            assertEquals(city, addressDTO.getCity());
            assertEquals(state, addressDTO.getState());
            assertEquals(zipCode, addressDTO.getZipCode());
            assertEquals(country, addressDTO.getCountry());
        }
    }

    public void createUser(EntityManagerHelper entityManagerHelper) {
        try (UserRepository userRepository = new UserRepository(entityManagerHelper)) {

            UserService userService = new UserService(userRepository);
            userService.processData("userForAddress", "userForAddress@w",
                    "userForAddress@google.com", "userForAddress", "userForAddress", "5511000000000", true, ProcessDataType.ADD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}