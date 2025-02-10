package test.resources;

import org.junit.Test;
import ostro.veda.common.ProcessDataType;
import ostro.veda.service.CommonService;
import ostro.veda.service.EntityType;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProductServiceTest {

    @Test
    public void processData() {
        /*
            Map<String, Map<String, Boolean>> categories = k:name = k:description v:isActive
            Map<String, Boolean> images = k:Url v:isMain
         */

        Map<String, Map<String, Boolean>> categories = Map.of("Furniture", Map.of("High quality handmade", true));
        Map<String, Boolean> images = Map.of("http://sub.example.co.uk/images/photo.png", true);

        assertNull(CommonService.productProcessData("four", "valid description",
                45.99, 15, false, categories, images, ProcessDataType.ADD, null));
        assertNull(CommonService.productProcessData("Mega Box", "valid description",
                -1, 15, true, categories, images, ProcessDataType.ADD, null));
        assertNull(CommonService.productProcessData("Ultra Coin", "valid description",
                0.0, -1, false, categories, images, ProcessDataType.ADD, null));


        assertNotNull(CommonService.productProcessData("Ultra Chair", "valid description",
                45.99, 15, false, categories, images, ProcessDataType.ADD, null));

        categories = Map.of(
                "Furniture", Map.of("High quality handmade", true),
                "Woodcraft", Map.of("Artisans work", true)
        );
        assertNotNull(CommonService.productProcessData("New Ultra Chair", "New valid description",
                49.99, 50, true, categories, images, ProcessDataType.UPDATE,
                Map.of(EntityType.PRODUCT, 1)));
    }
}
