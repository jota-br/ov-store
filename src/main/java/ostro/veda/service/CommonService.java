package ostro.veda.service;

import ostro.veda.common.dto.CategoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.dto.ProductImageDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonService {

    public static ProductDTO productProcessData(String nameProduct, String descriptionProduct, double priceProduct, int stockProduct, boolean isActiveProduct,
                                                Map<String, Map<String, Boolean>> categories, Map<String, Boolean> images) {
        /*
            Map<String, Map<String, Boolean>> categories = k:name = k:description v:isActive
            Map<String, Boolean> images = k:Url v:isMain
         */

        List<CategoryDTO> categoriesList = new ArrayList<>();
        for (Map.Entry<String, Map<String, Boolean>> entry : categories.entrySet()) {
            String name = entry.getKey();
            for (Map.Entry<String, Boolean> secondEntry : entry.getValue().entrySet()) {
                String description = secondEntry.getKey();
                boolean isActive = secondEntry.getValue();
                CategoryDTO category = CategoryService.processData(name, description, isActive);
                categoriesList.add(category);
            }
        }

        List<ProductImageDTO> imagesList = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : images.entrySet()) {
            String url = entry.getKey();
            boolean isMain = entry.getValue();
            ProductImageDTO productImageDTO = ProductImageService.processData(url, isMain);
            imagesList.add(productImageDTO);
        }

        return ProductService.processData(nameProduct, descriptionProduct, priceProduct, stockProduct, isActiveProduct, categoriesList, imagesList);
    }
}
