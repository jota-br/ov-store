package test.ostro.veda.test;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.dto.*;
import ostro.veda.db.*;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.AddressServiceImpl;
import ostro.veda.service.OrderServiceImpl;
import ostro.veda.service.ProductServiceImpl;
import ostro.veda.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderServiceImplTest {

    @Test
    public void addOrder() {

        ResetTables.resetTables();
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepositoryImpl orderRepositoryImpl = new OrderRepositoryImpl(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl(em2, categoryRepository);
                AddressRepositoryImpl addressRepository = new AddressRepositoryImpl(em2, new EntityManagerHelper())
        ) {

            UserServiceImpl userServiceImpl = new UserServiceImpl(userRepositoryImpl);
            ProductServiceImpl productServiceImpl = new ProductServiceImpl(productRepositoryImpl);
            OrderServiceImpl orderServiceImpl = new OrderServiceImpl(orderRepositoryImpl);
            AddressServiceImpl addressServiceImpl = new AddressServiceImpl(addressRepository);

            UserDTO user = getUserDTO(userServiceImpl);
            AddressDTO addressDTO = getAddressDTO(addressServiceImpl, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productServiceImpl, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), 5);
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.DELIVERED.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderServiceImpl.addOrder(orderBasic);
            assertNotNull(orderDTO);

            orderDTO = orderServiceImpl.addOrder(orderBasic);
            assertNotNull(orderDTO);

            orderDTO = orderServiceImpl.addOrder(orderBasic);
            Product pOne = orderRepositoryImpl.getEm().find(Product.class, productDTOList.get(0).getProductId());


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
                OrderRepositoryImpl orderRepositoryImpl = new OrderRepositoryImpl(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl(em2, categoryRepository);
                AddressRepositoryImpl addressRepository = new AddressRepositoryImpl(em2, new EntityManagerHelper())
        ) {

            UserServiceImpl userServiceImpl = new UserServiceImpl(userRepositoryImpl);
            ProductServiceImpl productServiceImpl = new ProductServiceImpl(productRepositoryImpl);
            OrderServiceImpl orderServiceImpl = new OrderServiceImpl(orderRepositoryImpl);
            AddressServiceImpl addressServiceImpl = new AddressServiceImpl(addressRepository);

            UserDTO user = getUserDTO(userServiceImpl);
            AddressDTO addressDTO = getAddressDTO(addressServiceImpl, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productServiceImpl, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), productDTOList.get(0).getStock());
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.DELIVERED.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderServiceImpl.addOrder(orderBasic);
            assertNotNull(orderDTO);

            orderDTO = orderServiceImpl.updateOrderStatus(1, OrderStatus.PROCESSING.getStatus());
            assertNotNull(orderDTO);
            assertEquals(orderDTO.getStatus(), OrderStatus.PROCESSING.getStatus());

        } catch (Exception ignored) {
        } finally {
            ResetTables.resetTables();
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
                OrderRepositoryImpl orderRepositoryImpl = new OrderRepositoryImpl(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl(em2, categoryRepository);
                AddressRepositoryImpl addressRepository = new AddressRepositoryImpl(em2, new EntityManagerHelper())
        ) {

            UserServiceImpl userServiceImpl = new UserServiceImpl(userRepositoryImpl);
            ProductServiceImpl productServiceImpl = new ProductServiceImpl(productRepositoryImpl);
            OrderServiceImpl orderServiceImpl = new OrderServiceImpl(orderRepositoryImpl);
            AddressServiceImpl addressServiceImpl = new AddressServiceImpl(addressRepository);

            UserDTO user = getUserDTO(userServiceImpl);
            AddressDTO addressDTO = getAddressDTO(addressServiceImpl, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productServiceImpl, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), 10);
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.PROCESSING.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderServiceImpl.addOrder(orderBasic);
            assertNotNull(orderDTO);

            OrderDTO orderToBeCancelled = orderServiceImpl.cancelOrder(orderDTO.getOrderId());
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
                OrderRepositoryImpl orderRepositoryImpl = new OrderRepositoryImpl(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl(em2, categoryRepository);
                AddressRepositoryImpl addressRepository = new AddressRepositoryImpl(em2, new EntityManagerHelper())
        ) {

            UserServiceImpl userServiceImpl = new UserServiceImpl(userRepositoryImpl);
            ProductServiceImpl productServiceImpl = new ProductServiceImpl(productRepositoryImpl);
            OrderServiceImpl orderServiceImpl = new OrderServiceImpl(orderRepositoryImpl);
            AddressServiceImpl addressServiceImpl = new AddressServiceImpl(addressRepository);

            UserDTO user = getUserDTO(userServiceImpl);
            AddressDTO addressDTO = getAddressDTO(addressServiceImpl, user);
            List<CategoryDTO> categories = getCategoryDTOS();
            List<ProductDTO> productDTOList = getProductDTOList(productServiceImpl, categories);

            OrderDetailBasic orderDetailBasic = new OrderDetailBasic(
                    productDTOList.get(0).getProductId(), productDTOList.get(0).getStock());
            OrderBasic orderBasic = new OrderBasic(user.getUserId(), OrderStatus.DELIVERED.getStatus(),
                    addressDTO.getAddressId(), addressDTO.getAddressId(), List.of(orderDetailBasic));

            OrderDTO orderDTO = orderServiceImpl.addOrder(orderBasic);

            orderDetailBasic = new OrderDetailBasic(
                    orderDTO.getOrderDetails().get(0).getProduct().getProductId(), orderDTO.getOrderDetails().get(0).getQuantity());
            orderBasic = new OrderBasic(user.getUserId(), orderDTO.getOrderId(), orderDTO.getStatus(),
                    orderDTO.getBillingAddress().getAddressId(), orderDTO.getShippingAddress().getAddressId(), List.of(orderDetailBasic));
            assertNotNull(orderDTO);

            OrderDTO orderAndItemToReturn = orderServiceImpl.returnItem(orderBasic);
            assertNotNull(orderAndItemToReturn);
            assertEquals(orderAndItemToReturn.getStatus(), OrderStatus.RETURN_REQUESTED.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static List<ProductDTO> getProductDTOList(ProductServiceImpl productServiceImpl, List<CategoryDTO> categories) {
        return List.of(
                productServiceImpl.addProduct(new ProductDTO(0, "Product Test One", "Description One",
                        45.00, 10, true, categories, null,  null, null, 0)),
                productServiceImpl.addProduct(new ProductDTO(0, "Product Test Two", "Description Two",
                        50.00, 5, true, categories, null,  null, null, 0))
        );
    }

    private static List<ProductImageDTO> getProductImageDTOS() {
        List<ProductImageDTO> images = new ArrayList<>();
        images.add(new ProductImageDTO(0, "http://sub.example.co.uk/images/photo.png", true, 0));
        return images;
    }

    private static List<CategoryDTO> getCategoryDTOS() {
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(0, "Furniture", "Handmade", true, null, null, 0));
        categories.add(new CategoryDTO(0, "Wood work", "Artisan", true,  null, null, 0));
        return categories;
    }

    private static UserDTO getUserDTO(UserServiceImpl userServiceImpl) {

        return userServiceImpl.add(new UserDTO(0, "username90R", null, null,
                "email@example.com", "John", "Doe", "+00099988877766",
                true, getRoleDTO(), null, null, null, 0), "password");
    }

    private static RoleDTO getRoleDTO() {
        return new RoleDTO(20, null, null, null, null, null, 0);
    }

    private static AddressDTO getAddressDTO(AddressServiceImpl addressServiceImpl, UserDTO user) {
        return addressServiceImpl.add(new AddressDTO(0, user.getUserId(), "Street N123", "1900-B",
                "Home", "Hollville", "State of Play", "900103041", "Brazil", true, null, null, 0));
    }
}