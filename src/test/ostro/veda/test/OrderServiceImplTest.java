package ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.*;
import ostro.veda.config.AppConfig;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.service.OrderServiceImpl;
import ostro.veda.service.ProductServiceImpl;
import ostro.veda.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class OrderServiceImplTest {

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

    private ProductDTO getProductDTO() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        CategoryDTO categoryDTO = new CategoryDTO(0, "Category Name", "Category Description",
                true, null, null, 0);
        categoryDTOS.add(categoryDTO);

        List<ProductImageDTO> productImageDTOS = new ArrayList<>();
        ProductImageDTO productImageDTO = new ProductImageDTO(0,
                "https://imagesemxaple.com/image.png", true, 0);
        productImageDTOS.add(productImageDTO);

        return new ProductDTO(0, "Product One", "Description One",
                99.99,10, true, categoryDTOS, productImageDTOS, null, null, 0);
    }

    private OrderDetailDTO getOrderDetail(OrderDTO orderDTO, ProductDTO productDTO) {
        return new OrderDetailDTO(0, orderDTO, productDTO,
                1, productDTO.getPrice(), 0);
    }

    private OrderDTO getOrder(ProductDTO productDTO, AddressDTO addressDTO, int userId) {

        OrderDTO orderDTO = new OrderDTO(0, userId, null, 0,
                OrderStatus.PENDING_PAYMENT.getStatus(), List.of(), addressDTO, addressDTO,
                null, null, 0);

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(0, orderDTO, productDTO,
                3, productDTO.getPrice(), 0);

        orderDTO.setOrderDetails(List.of(orderDetailDTO));

        return orderDTO;
    }

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);
        OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class);

        UserDTO userDTO = getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
        orderDTO = orderService.add(orderDTO);
        assertNotNull(orderDTO);

        // close context (container)
        context.close();
    }

    @Test
    public void update() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);
        OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class);

        UserDTO userDTO = getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
        orderDTO = orderService.add(orderDTO);
        orderDTO = orderService.update(new OrderDTO(orderDTO.getOrderId(), orderDTO.getUserId(),
                null, 0, OrderStatus.PROCESSING.getStatus(), null, null,
                null, null, null, 0));
        assertNotNull(orderDTO);

        // close context (container)
        context.close();
    }

    @Test
    public void cancelOrder() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);
        OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class);

        UserDTO userDTO = getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
        orderDTO = orderService.add(orderDTO);
        orderDTO = orderService.cancelOrder(orderDTO.getOrderId());
        assertNotNull(orderDTO);

        // close context (container)
        context.close();
    }

    @Test
    public void returnItem() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);
        OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class);

        UserDTO userDTO = getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
        orderDTO = orderService.add(orderDTO);

        OrderDetailDTO orderDetailDTO = getOrderDetail(orderDTO, productDTO);
        orderDTO = orderService.update(new OrderDTO(orderDTO.getOrderId(), orderDTO.getUserId(),
                null, 0, OrderStatus.DELIVERED.getStatus(), null, null,
                null, null, null, 0));
        orderDTO = orderService.returnItem(orderDetailDTO);
        assertNotNull(orderDTO);

        // close context (container)
        context.close();
    }
}
