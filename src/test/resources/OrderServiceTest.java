package test.resources;

import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.OrderDetailRepository;
import ostro.veda.db.OrderRepository;
import ostro.veda.db.UserRepository;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.User;
import ostro.veda.service.OrderDetailService;
import ostro.veda.service.OrderService;
import ostro.veda.service.UserService;

public class OrderServiceTest {

    @Test
    public void processData() {
        EntityManagerHelper entityManagerHelper = new EntityManagerHelper();
        try (
                OrderRepository orderRepository = new OrderRepository(entityManagerHelper);
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(entityManagerHelper);
                UserRepository userRepository = new UserRepository(entityManagerHelper)
        ) {
            UserService userService = new UserService(userRepository);
            OrderDetailService orderDetailService = new OrderDetailService(orderDetailRepository);
            OrderService orderService = new OrderService(orderRepository, orderDetailService);

            String username = "orderServiceUser";
            String password = "orderServiceUser@93";
            String email = "orderServiceUser@example.com";
            String firstName = "orderServiceUser";
            String lastName = "orderService";
            String phone = "5511000000000";

            UserDTO user = userService.processData(-1, username, password,
                    email, firstName, lastName, phone, true, ProcessDataType.ADD);

//            int userId, double totalAmount, OrderStatus status, Address shippingAddress,
//                    Address billingAddress, Map< ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType

            orderService.processData(user.getUserId())
        } catch (Exception ignored) {
        }

    }
}