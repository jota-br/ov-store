package ostro.veda.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.validation.annotation.ValidDescription;
import ostro.veda.util.validation.annotation.ValidPrice;
import ostro.veda.util.validation.annotation.ValidStock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.PRODUCT)
public class ProductDto implements Dto {

    private final int productId;

    @NotBlank(message = "Name cannot be blank")
    private final String name;

    @ValidDescription
    private final String description;

    @ValidPrice
    private final double price;

    @ValidStock
    private final int stock;

    private final boolean isActive;

    @Valid
    private final List<CategoryDto> categories;

    @Valid
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
