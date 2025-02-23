package test.resources;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.*;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.*;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OrderServiceTest {

    @Test
    public void addOrder() {
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                ProductRepository productRepository = new ProductRepository(em2);
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductImageRepository productImageRepository = new ProductImageRepository(em2);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);
            OrderService orderService = new OrderService(orderRepository);

            UserDTO user = getUserDTO(userService, "username1", "password99*", "email@email.com",
                    "5511111000999");
            AddressService addressService = new AddressService(addressRepository);
            AddressDTO addressDTO = getAddressDTO(addressService, user
            );
            Map<String, String> categories = Map.of("Test Category", "Another Test Category");
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            int itemOneQty = 1;
            int itemTwoQty = 1;
            double total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            int remainingStockOne = productDTOList.get(0).getStock() - itemOneQty;
            int remainingStockTwo = productDTOList.get(1).getStock() - itemTwoQty;

            OrderDTO orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.PENDING_PAYMENT.getStatus(), addressDTO, addressDTO,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));
            assertNotNull(orderDTO);

            itemOneQty = 2;
            itemTwoQty = 2;
            total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            remainingStockOne = remainingStockOne - itemOneQty;
            remainingStockTwo = remainingStockTwo - itemTwoQty;
            orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.DRAFT.getStatus(), addressDTO, addressDTO,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));
            assertNotNull(orderDTO);

            // Product Test Two has no sufficient inventory for this transaction
            itemOneQty = 3;
            itemTwoQty = 3;
            total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.PENDING_PAYMENT.getStatus(), addressDTO, addressDTO,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));
            Product pOne = orderRepository.getEm().find(Product.class, productDTOList.get(0).getProductId());
            Product pOTwo = orderRepository.getEm().find(Product.class, productDTOList.get(1).getProductId());

            assertNull(orderDTO);
            assertEquals(remainingStockOne, pOne.getStock());
            assertEquals(remainingStockTwo, pOTwo.getStock());
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
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
                ProductRepository productRepository = new ProductRepository(em2);
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductImageRepository productImageRepository = new ProductImageRepository(em2);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);
            OrderService orderService = new OrderService(orderRepository);

            UserDTO user = getUserDTO(userService, "username12", "password99*", "email2@email.com",
                    "5511111000998");
            AddressService addressService = new AddressService(addressRepository);
            AddressDTO addressDTO = getAddressDTO(addressService, user
            );
            Map<String, String> categories = Map.of("Test Category", "Another Test Category");
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            int itemOneQty = 1;
            int itemTwoQty = 1;
            double total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);

            OrderDTO orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.PENDING_PAYMENT.getStatus(), addressDTO, addressDTO,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));
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
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                ProductRepository productRepository = new ProductRepository(em2);
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductImageRepository productImageRepository = new ProductImageRepository(em2);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);
            OrderService orderService = new OrderService(orderRepository);

            UserDTO user = getUserDTO(userService, "username123", "password99*33", "email23@email.com",
                    "5511111000997");
            AddressService addressService = new AddressService(addressRepository);
            AddressDTO addressDTO = getAddressDTO(addressService, user
            );
            Map<String, String> categories = Map.of("Test Category", "Another Test Category");
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            int itemOneQty = 1;
            int itemTwoQty = 1;
            double total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            OrderDTO orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.PENDING_PAYMENT.getStatus(), addressDTO, addressDTO,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));
            assertNotNull(orderDTO);

            OrderDTO orderToBeCancelled = orderService.cancelOrder(orderDTO.getOrderId());
            assertNotNull(orderToBeCancelled);
            assertEquals(orderToBeCancelled.getStatus(), OrderStatus.CANCELLED.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }

    @Test
    public void returnItem() {
        EntityManager em = JPAUtil.getEm();
        EntityManager em2 = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                ProductRepository productRepository = new ProductRepository(em2);
                CategoryRepository categoryRepository = new CategoryRepository(em2);
                ProductImageRepository productImageRepository = new ProductImageRepository(em2);
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);
            OrderService orderService = new OrderService(orderRepository);

            UserDTO user = getUserDTO(userService, "username123", "password99*33", "email23@email.com",
                    "5511111000997");
            AddressService addressService = new AddressService(addressRepository);
            AddressDTO addressDTO = getAddressDTO(addressService, user
            );
            Map<String, String> categories = Map.of("Test Category", "Another Test Category");
            List<ProductDTO> productDTOList = getProductDTOList(productService, categories);

            int itemOneQty = 1;
            int itemTwoQty = 1;
            double total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            OrderDTO orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.DELIVERED.getStatus(), addressDTO, addressDTO,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));
            assertNotNull(orderDTO);

            OrderDTO orderAndItemToReturn = orderService.returnItem(orderDTO.getOrderId(), Map.of(productDTOList.get(0), 1));
            assertNotNull(orderAndItemToReturn);
            assertEquals(orderAndItemToReturn.getStatus(), OrderStatus.RETURN_REQUESTED.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            ResetTables.resetTables();
        }
    }

    private static UserDTO getUserDTO(UserService userService, String username, String password, String email,
                                      String phone) {

        return userService.processData(-1, username, password,
                email, "Hobart", "Shulz", phone, true, ProcessDataType.ADD);
    }

    private static AddressDTO getAddressDTO(AddressService addressService, UserDTO user) {
        int id = -1;
        return addressService.processData(id, user.getUserId(), "streetname", "1900-B",
                "Home", "Joinville", "Santa Catarina", "900103041", "Brazil", true, ProcessDataType.ADD);
    }

    private static List<ProductDTO> getProductDTOList(ProductService productService, Map<String, String> categories) {
        return List.of(
                productService.addProduct("Product Test One", "Description One",
                        45.00, 10, true, categories, null),
                productService.addProduct("Product Test Two", "Description Two",
                        50.00, 5, true, categories, null)
        );
    }
}