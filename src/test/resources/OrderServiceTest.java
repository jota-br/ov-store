package test.resources;

import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.*;
import ostro.veda.db.*;
import ostro.veda.db.helpers.EntityManagerHelper;
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
        try (
                OrderRepository orderRepository = new OrderRepository(entityManagerHelper);
                OrderDetailRepository orderDetailRepository = new OrderDetailRepository(entityManagerHelper);
                UserRepository userRepository = new UserRepository(entityManagerHelper);
                ProductRepository productRepository = new ProductRepository(entityManagerHelper);
                CategoryRepository categoryRepository = new CategoryRepository(entityManagerHelper);
                ProductImageRepository productImageRepository = new ProductImageRepository(entityManagerHelper);
                AddressRepository addressRepository = new AddressRepository(entityManagerHelper)
        ) {
            UserService userService = new UserService(userRepository);
            CategoryService categoryService = new CategoryService(categoryRepository);
            ProductImageService productImageService = new ProductImageService(productImageRepository);
            ProductService productService = new ProductService(categoryService, productImageService, productRepository);
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

            Map<OrderDTO, List<OrderDetailDTO>> orderDTOListMap = orderService.processData(user.getUserId(), total, OrderStatus.DRAFT, address, address,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty), ProcessDataType.ADD);

            assertNotNull(orderDTOListMap);

            itemOneQty = 2;
            itemTwoQty = 2;
            total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            remainingStockOne = remainingStockOne - itemOneQty;
            remainingStockTwo = remainingStockTwo - itemTwoQty;
            orderDTOListMap = orderService.processData(user.getUserId(), total, OrderStatus.DRAFT, address, address,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty), ProcessDataType.ADD);

            assertNotNull(orderDTOListMap);

            // Product Test Two has no sufficient inventory for this transaction
            itemOneQty = 3;
            itemTwoQty = 3;
            total = (productDTOList.get(0).getPrice() * itemOneQty) + (productDTOList.get(1).getPrice() * itemTwoQty);
            orderDTOListMap = orderService.processData(user.getUserId(), total, OrderStatus.DRAFT, address, address,
                    Map.of(productDTOList.get(0), itemOneQty, productDTOList.get(1), itemTwoQty), ProcessDataType.ADD);
            Product pOne = orderRepository.getEm().find(Product.class, productDTOList.get(0).getProductId());
            Product pOTwo = orderRepository.getEm().find(Product.class, productDTOList.get(1).getProductId());

            assertNull(orderDTOListMap);
            assertEquals(remainingStockOne, pOne.getStock());
            assertEquals(remainingStockTwo, pOTwo.getStock());
        } catch (Exception ignored) {
        }

    }
}