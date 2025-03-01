package test.ostro.veda.test;

import ostro.veda.common.dto.*;
import ostro.veda.service.AddressServiceImpl;
import ostro.veda.service.ProductServiceImpl;
import ostro.veda.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<ProductDTO> getProductDTOList(ProductServiceImpl productServiceImpl, List<CategoryDTO> categories) {
        return List.of(
                productServiceImpl.addProduct(new ProductDTO(0, "Product Test One", "Description One",
                        45.00, 10, true, categories, null,  null, null, 0)),
                productServiceImpl.addProduct(new ProductDTO(0, "Product Test Two", "Description Two",
                        50.00, 5, true, categories, null, null, null, 0))
        );
    }

    public static List<ProductImageDTO> getProductImageDTOS() {
        List<ProductImageDTO> images = new ArrayList<>();
        images.add(new ProductImageDTO(0, "http://sub.example.co.uk/images/photo.png", true, 0));
        return images;
    }

    public static List<CategoryDTO> getCategoryDTOS() {
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(0, "Furniture", "Handmade", true,  null, null, 0));
        categories.add(new CategoryDTO(0, "Wood work", "Artisan", true,  null, null, 0));
        return categories;
    }

    public static UserDTO getUserDTO(UserServiceImpl userServiceImpl) {

        return userServiceImpl.add(new UserDTO(0, "username90R", null, null,
                "email@example.com", "John", "Doe", "+00099988877766",
                true, getRoleDTO(), List.of(), null, null, 0), "password");
    }

    public static RoleDTO getRoleDTO() {
        return new RoleDTO(20, null, null, null, null, null, 0);
    }

    public static AddressDTO getAddressDTO(AddressServiceImpl addressServiceImpl, UserDTO user) {
        return addressServiceImpl.add(new AddressDTO(0, user.getUserId(), "Street N123", "1900-B",
                "Home", "Hollville", "State of Play", "900103041", "Brazil", true, null, null, 0));
    }
}
