package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class ProductImageDTO {

    private final int productImageId;
    private final String imageUrl;
    private final boolean isMain;
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"productImageId\":" + productImageId)
                .add("\"imageUrl\":\"" + imageUrl + "\"")
                .add("\"isMain\":" + isMain)
                .add("\"version\":" + version)
                .toString();
    }
}
