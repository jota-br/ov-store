package test.resources;

import ostro.veda.common.dto.*;
import ostro.veda.service.AddressService;
import ostro.veda.service.ProductService;
import ostro.veda.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<ProductDTO> getProductDTOList(ProductService productService, List<CategoryDTO> categories) {
        return List.of(
                productService.addProduct(new ProductDTO(0, "Product Test One", "Description One",
                        45.00, 10, true, categories, null)),
                productService.addProduct(new ProductDTO(0, "Product Test Two", "Description Two",
                        50.00, 5, true, categories, null))
        );
    }

    public static List<ProductImageDTO> getProductImageDTOS() {
        List<ProductImageDTO> images = new ArrayList<>();
        images.add(new ProductImageDTO(0, "http://sub.example.co.uk/images/photo.png", true));
        return images;
    }

    public static List<CategoryDTO> getCategoryDTOS() {
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(0, "Furniture", "Handmade", true));
        categories.add(new CategoryDTO(0, "Wood work", "Artisan", true));
        return categories;
    }

    public static UserDTO getUserDTO(UserService userService) {

        return userService.addUser(new UserDTO(0, "username90R", null, null,
                "email@example.com", "John", "Doe", "+00099988877766",
                true, getRoleDTO(), null, null, null), "password");
    }

    public static RoleDTO getRoleDTO() {
        return new RoleDTO(20, null, null, null, null, null);
    }

    public static AddressDTO getAddressDTO(AddressService addressService, UserDTO user) {
        return addressService.addAddress(new AddressDTO(0, user.getUserId(), "Street N123", "1900-B",
                "Home", "Hollville", "State of Play", "900103041", "Brazil", true, null, null));
    }
}
