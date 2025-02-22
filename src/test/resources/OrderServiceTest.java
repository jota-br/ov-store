package test;

import jakarta.persistence.EntityManager;
import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.*;
import ostro.veda.db.*;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Product;
import ostro.veda.service.*;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OrderServiceTest {

    @Test
    public void processData() {
        EntityManagerHelper entityManagerHelper = new EntityManagerHelper();
        EntityManager em = JPAUtil.getEm();
        try (
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(em);
                OrderStatusHistoryRepository orderStatusHistoryRepository = new OrderStatusHistoryRepository(em);
                OrderRepository orderRepository = new OrderRepository(em, orderDetailRepository, orderStatusHistoryRepository);
                UserRepository userRepository = new UserRepository();
                ProductRepository productRepository = new ProductRepository();
                CategoryRepository categoryRepository = new CategoryRepository();
                ProductImageRepository productImageRepository = new ProductImageRepository();
                AddressRepository addressRepository = new AddressRepository()
        ) {

            UserService userService = new UserService(userRepository);
            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);
            OrderService orderService = new OrderService(orderRepository);

            String username = "orderServiceUser";
            String password = "orderServiceUser@93";
            String email = "orderServiceUser@example.com";
            String firstName = "orderServiceUser";
            String lastName = "orderService";
            String phone = "5511000000099";

            UserDTO user = userService.processData(-1, username, password,
                    email, firstName, lastName, phone, true, ProcessDataType.ADD);

            AddressService addressService = new AddressService(addressRepository);

            int id = -1;
            String street = "XV de Novembro";
            String addressNumber = "10900";
            String addressType = "Home";
            String city = "Joinville";
            String state = "Santa Catarina";
            String zipCode = "00000000";
            String country = "Brazil";


            AddressDTO addressDTO = addressService.processData(id, user.getUserId(), street, addressNumber,
                    addressType, city, state, zipCode, country, true, ProcessDataType.ADD);

            List<String> categories = List.of("Test Category", "Another Test Category");

            List<ProductDTO> productDTOList = List.of(
                    productService.processData("Product Test One", "Description One",
                            45.00, 10, true, categories, null, ProcessDataType.ADD,
                            null),
                    productService.processData("Product Test Two", "Description Two",
                            50.00, 5, true, categories, null, ProcessDataType.ADD,
                            null)
            );

            int itemOneQty = 1;
            int itemTwoQty = 1;
            double total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            int remainingStockOne = productDTOList.get(0).getStock() - itemOneQty;
            int remainingStockTwo = productDTOList.get(1).getStock() - itemTwoQty;
            Address address = orderRepository.getEm().find(Address.class, addressDTO.getAddressId());

            OrderDTO orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.PENDING_PAYMENT.getStatus(), address, address,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));

            assertNotNull(orderDTO);

            itemOneQty = 2;
            itemTwoQty = 2;
            total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            remainingStockOne = remainingStockOne - itemOneQty;
            remainingStockTwo = remainingStockTwo - itemTwoQty;
            orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.DRAFT.getStatus(), address, address,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));

            assertNotNull(orderDTO);

            orderDTO = orderService.updateOrderStatus(orderDTO.getOrderId(), OrderStatus.PROCESSING.getStatus());
            assertNotNull(orderDTO);
            assertEquals(orderDTO.getStatus(), OrderStatus.PROCESSING.getStatus());

            orderDTO = orderService.cancelOrder(orderDTO.getOrderId());
            assertNotNull(orderDTO);
            assertEquals(orderDTO.getStatus(), OrderStatus.CANCELLED.getStatus());

            // Product Test Two has no sufficient inventory for this transaction
            itemOneQty = 3;
            itemTwoQty = 3;
            total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            orderDTO = orderService.addOrder(user.getUserId(), total, OrderStatus.PENDING_PAYMENT.getStatus(), address, address,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty));
            Product pOne = orderRepository.getEm().find(Product.class, productDTOList.get(0).getProductId());
            Product pOTwo = orderRepository.getEm().find(Product.class, productDTOList.get(1).getProductId());

            assertNull(orderDTO);
            assertEquals(remainingStockOne, pOne.getStock());
            assertEquals(remainingStockTwo, pOTwo.getStock());
        } catch (Exception ignored) {
        }

    }
}