package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableNames;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.PRODUCT)
public class ProductDto implements Dto {

    private final int productId;
    private final String name;
    private final String description;
    private final double price;
    private final int stock;

    private final boolean isActive;

    private final List<CategoryDto> categories;
    private final List<ProductImageDto> images;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

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
                .add("\"createdAt\":\"" + createdAt + "\"")
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }
}
