package ostro.veda.common.dto;

public class OrderDetailBasic {

    private final int productId;
    private final int quantity;

    public OrderDetailBasic(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
