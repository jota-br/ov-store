package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.common.util.Auditable;
import ostro.veda.common.util.TableNames;

import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.PRODUCT_IMAGE)
public class ProductImageDTO {

    private final int productImageId;
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
