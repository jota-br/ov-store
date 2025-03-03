package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProductDTO {

    private final int productId;
    private final String name;
    private final String description;
    private final double price;
    private final int stock;
    private final boolean isActive;
    private final List<CategoryDTO> categories;
    private final List<ProductImageDTO> images;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;
}
