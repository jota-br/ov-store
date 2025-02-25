package ostro.veda.common.dto;

public class ProductImageDTO {

    private final int productImageId;
    private final String imageUrl;
    private final boolean isMain;

    /**
     *
     * @param productImageId int
     * @param imageUrl String
     * @param isMain boolean
     */
    public ProductImageDTO(int productImageId, String imageUrl, boolean isMain) {
        this.productImageId = productImageId;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }

    /**
     *
     * @param imageUrl String
     * @param isMain boolean
     */
    public ProductImageDTO(String imageUrl, boolean isMain) {
        this(-1, imageUrl, isMain);
    }

    // GETTERS
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
