package ostro.veda.common.dto;

public class OrderDetailDTO {

    private final int orderDetailId;
    private final OrderDTO order;
    private final ProductDTO product;
    private final int quantity;
    private final double unitPrice;

    public OrderDetailDTO(int orderDetailId, OrderDTO order, ProductDTO product, int quantity, double unitPrice) {
        this.orderDetailId = orderDetailId;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderDetailDTO(OrderDTO order, ProductDTO product, int quantity, double unitPrice) {
        this(0, order, product, quantity, unitPrice);
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}
