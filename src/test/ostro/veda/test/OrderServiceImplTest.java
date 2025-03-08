package ostro.veda.test;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.*;
import ostro.veda.common.validation.OrderStatus;
import ostro.veda.config.AppConfig;
import ostro.veda.service.CouponServiceImpl;
import ostro.veda.service.OrderServiceImpl;
import ostro.veda.service.ProductServiceImpl;
import ostro.veda.service.UserServiceImpl;

import static org.junit.Assert.assertNotNull;

public class OrderServiceImplTest {

    private static Helper helper = new Helper();

    @Test
    public void add() {

        ResetTables.resetTables();
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserServiceImpl userService = context.getBean(UserServiceImpl.class);
        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);
        OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class);
        CouponServiceImpl couponService = context.getBean(CouponServiceImpl.class);

        UserDTO userDTO = helper.getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = helper.getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = helper.getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
        orderDTO = orderService.add(orderDTO);
        assertNotNull(orderDTO);

        CouponDTO couponDTO = couponService.add(helper.getCouponDTO());
        orderDTO = orderService.add(helper.getOrderWithCoupon(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId(), couponDTO));
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

        UserDTO userDTO = helper.getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = helper.getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = helper.getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
        orderDTO = orderService.add(orderDTO);
        orderDTO = orderService.update(new OrderDTO(orderDTO.getOrderId(), orderDTO.getUserId(),
                null, 0, OrderStatus.PROCESSING.getStatus(), null, null,
                null, null, null, null, 0));
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

        UserDTO userDTO = helper.getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = helper.getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = helper.getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
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

        UserDTO userDTO = helper.getUserDTO();
        userDTO = userService.add(userDTO, "password123*@");
        assertNotNull(userDTO);

        ProductDTO productDTO = helper.getProductDTO();
        productDTO = productService.add(productDTO);
        assertNotNull(productDTO);

        OrderDTO orderDTO = helper.getOrder(productDTO, userDTO.getAddresses().get(0), userDTO.getUserId());
        orderDTO = orderService.add(orderDTO);

        OrderDetailDTO orderDetailDTO = helper.getOrderDetail(orderDTO, productDTO);
        orderDTO = orderService.update(new OrderDTO(orderDTO.getOrderId(), orderDTO.getUserId(),
                null, 0, OrderStatus.DELIVERED.getStatus(), null, null,
                null, null, null, null, 0));
        orderDTO = orderService.returnItem(orderDetailDTO);
        assertNotNull(orderDTO);

        // close context (container)
        context.close();
    }
}
