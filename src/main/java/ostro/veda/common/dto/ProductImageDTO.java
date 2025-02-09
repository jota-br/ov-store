package ostro.veda.common.dto;

public class ProductImageDTO {

    private final int productImageId;
    private final String imageUrl;
    private final boolean isMain;

    public ProductImageDTO(int productImageId, String imageUrl, boolean isMain) {
        this.productImageId = productImageId;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }

    public int getProductImageId() {
        return productImageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isMain() {
        return isMain;
    }
}
