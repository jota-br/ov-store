package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

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

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"productId\":" + productId)
                .add("\"name\":\"" + name + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"price\":" + price)
                .add("\"stock\":" + stock)
                .add("\"isActive\":" + isActive)
                .add("\"categories\":" + categories)
                .add("\"images\":" + images)
                .add("\"createdAt\":" + createdAt)
                .add("\"updatedAt\":" + updatedAt)
                .toString();
    }
}
