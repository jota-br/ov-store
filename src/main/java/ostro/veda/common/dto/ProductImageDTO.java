package main.java.ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductImageDTO {

    private final int productImageId;
    private final String imageUrl;
    private final boolean isMain;
    private final int version;
}
