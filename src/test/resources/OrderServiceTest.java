package test.resources;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.dto.*;
import ostro.veda.db.*;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.AddressService;
import ostro.veda.service.OrderService;
import ostro.veda.service.ProductService;
import ostro.veda.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderServiceTest {

    @Test
    public void addOrder() {

        ResetTables.resetTables();
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepository productRepository = new ProductRepository(em2, categoryRepository);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            ProductService productService = new ProductService(productRepository);
            OrderService orderService = new OrderService(orderRepository);
            AddressService addressService = new AddressService(addressRepository);

            UserDTO user = getUserDTO(userService);
            AddressDTO addressDTO = getAddressDTO(addressService, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), 5);
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.DELIVERED.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderService.addOrder(orderBasic);
            assertNotNull(orderDTO);

            orderDTO = orderService.addOrder(orderBasic);
            assertNotNull(orderDTO);

            orderDTO = orderService.addOrder(orderBasic);
            Product pOne = orderRepository.getEm().find(Product.class, productDTOList.get(0).getProductId());


            assertNull(orderDTO);
            assertEquals(0, pOne.getStock());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateOrderStatus() {
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepository productRepository = new ProductRepository(em2, categoryRepository);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            ProductService productService = new ProductService(productRepository);
            OrderService orderService = new OrderService(orderRepository);
            AddressService addressService = new AddressService(addressRepository);

            UserDTO user = getUserDTO(userService);
            AddressDTO addressDTO = getAddressDTO(addressService, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), productDTOList.get(0).getStock());
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.DELIVERED.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderService.addOrder(orderBasic);
            assertNotNull(orderDTO);

            orderDTO = orderService.updateOrderStatus(1, OrderStatus.PROCESSING.getStatus());
            assertNotNull(orderDTO);
            assertEquals(orderDTO.getStatus(), OrderStatus.PROCESSING.getStatus());

        } catch (Exception ignored) {
        } finally {
            test.resources.ResetTables.resetTables();
        }
    }

    @Test
    public void cancelOrder() {

        ResetTables.resetTables();
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepository productRepository = new ProductRepository(em2, categoryRepository);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            ProductService productService = new ProductService(productRepository);
            OrderService orderService = new OrderService(orderRepository);
            AddressService addressService = new AddressService(addressRepository);

            UserDTO user = getUserDTO(userService);
            AddressDTO addressDTO = getAddressDTO(addressService, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), 10);
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.PROCESSING.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderService.addOrder(orderBasic);
            assertNotNull(orderDTO);

            OrderDTO orderToBeCancelled = orderService.cancelOrder(orderDTO.getOrderId());
            assertNotNull(orderToBeCancelled);
            assertEquals(orderToBeCancelled.getStatus(), OrderStatus.CANCELLED.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void returnItem() {

        ResetTables.resetTables();
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepository productRepository = new ProductRepository(em2, categoryRepository);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            ProductService productService = new ProductService(productRepository);
            OrderService orderService = new OrderService(orderRepository);
            AddressService addressService = new AddressService(addressRepository);

            UserDTO user = getUserDTO(userService);
            AddressDTO addressDTO = getAddressDTO(addressService, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), productDTOList.get(0).getStock());
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.DELIVERED.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderService.addOrder(orderBasic);

            orderDetailBasic = new OrderDetailBasic(
                    orderDTO.getOrderDetails().get(0).getProduct().getProductId(), orderDTO.getOrderDetails().get(0).getQuantity());
            orderBasic = new OrderBasic(user.getUserId(), orderDTO.getOrderId(), orderDTO.getStatus(),
                    orderDTO.getBillingAddress().getAddressId(), orderDTO.getShippingAddress().getAddressId(), List.of(orderDetailBasic));
            assertNotNull(orderDTO);

            OrderDTO orderAndItemToReturn = orderService.returnItem(orderBasic);
            assertNotNull(orderAndItemToReturn);
            assertEquals(orderAndItemToReturn.getStatus(), OrderStatus.RETURN_REQUESTED.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static List<ProductDTO> getProductDTOList(ProductService productService, List<CategoryDTO> categories) {
        return List.of(
                productService.addProduct(new ProductDTO(0, "Product Test One", "Description One",
                        45.00, 10, true, categories, null)),
                productService.addProduct(new ProductDTO(0, "Product Test Two", "Description Two",
                        50.00, 5, true, categories, null))
        );
    }

    private static List<ProductImageDTO> getProductImageDTOS() {
        List<ProductImageDTO> images = new ArrayList<>();
        images.add(new ProductImageDTO(0, "http://sub.example.co.uk/images/photo.png", true));
        return images;
    }

    private static List<CategoryDTO> getCategoryDTOS() {
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(0, "Furniture", "Handmade", true));
        categories.add(new CategoryDTO(0, "Wood work", "Artisan", true));
        return categories;
    }

    private static UserDTO getUserDTO(UserService userService) {

        return userService.addUser(new UserDTO(0, "username90R", null, null,
                "email@example.com", "John", "Doe", "+00099988877766",
                true, getRoleDTO(), null, null, null), "password");
    }

    private static RoleDTO getRoleDTO() {
        return new RoleDTO(20, null, null, null, null, null);
    }

    private static AddressDTO getAddressDTO(AddressService addressService, UserDTO user) {
        return addressService.addAddress(new AddressDTO(0, user.getUserId(), "Street N123", "1900-B",
                "Home", "Hollville", "State of Play", "900103041", "Brazil", true, null, null));
    }
}