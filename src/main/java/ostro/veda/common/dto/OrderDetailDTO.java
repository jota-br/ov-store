package ostro.veda.common.dto;

import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.Product;

public class OrderDetailDTO {

    private final int orderDetailId;
    private final Order order;
    private final Product product;
    private final int quantity;
    private final double unitPrice;

    public OrderDetailDTO(int orderDetailId, Order order, Product product, int quantity, double unitPrice) {
        this.orderDetailId = orderDetailId;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}
