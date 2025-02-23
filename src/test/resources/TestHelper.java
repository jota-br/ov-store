package test.resources;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductImageDTO;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<ProductImageDTO> getProductImageDTOS() {
        List<ProductImageDTO> images = new ArrayList<>();
        images.add(new ProductImageDTO(-1, "http://sub.example.co.uk/images/photo.png", true));
        return images;
    }

    public static List<CategoryDTO> getCategoryDTOS() {
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO(-1, "Furniture", "Handmade", true));
        categories.add(new CategoryDTO(-1, "Wood work", "Artisan", true));
        return categories;
    }
}
