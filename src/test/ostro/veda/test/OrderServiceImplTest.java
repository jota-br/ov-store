package test.ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.*;
import ostro.veda.config.AppConfig;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImplTest {

    private UserDTO getUserDTO() {
        AddressDTO addressDTO = new AddressDTO(0, 0, "Street 234 West",
                "123-B", "Home", "Some City Name", "State of Where",
                "90222000", "The United Country", true, null, null, 0);

        RoleDTO roleDTO = new RoleDTO(20, null, null,
                null, null, null, 0);

        return new UserDTO(0, "username92", null, null,
                "email@example.com", "John", "Doe", "+5511122233344",
                true, roleDTO, List.of(addressDTO), null, null, 0);
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

    private OrderDTO getOrder(ProductDTO productDTO, AddressDTO addressDTO, int userId) {

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(0, null, productDTO,
                3, productDTO.getPrice(), 0);

        return new OrderDTO(0, userId, null, 0,
                OrderStatus.PENDING_PAYMENT.getStatus(), List.of(orderDetailDTO), addressDTO, addressDTO,
                null, null, 0);
    }

    @Test
    public void add() {
        // create context (container)
        ConfigurableApplicationContext context
                = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        userService.add(getUserDTO(), "password123*@");


//        OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class);
//
//        orderService.add(orderDTO);

        // close context (container)
        context.close();
    }

    @Test
    public void update() {
    }

    @Test
    public void cancelOrder() {
    }

    @Test
    public void returnItem() {
    }
}