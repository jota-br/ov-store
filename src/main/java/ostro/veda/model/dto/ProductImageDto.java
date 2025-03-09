package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.validation.annotation.ValidImage;

import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.PRODUCT_IMAGE)
public class ProductImageDto implements Dto {

    private final int productImageId;

    @ValidImage
    private final String imageUrl;

    private final boolean isMain;
    private final int version;


    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"productImageId\":" + productImageId)
                .add("\"imageUrl\":\"" + imageUrl + "\"")
                .add("\"isMain\":" + isMain)
                .add("\"version\":" + version)
                .toString();
    }
}
