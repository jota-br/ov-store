package ostro.veda.db.jpa;

import jakarta.persistence.*;
import ostro.veda.common.dto.OrderDetailDTO;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private int orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    public OrderDetail() {
    }

    public OrderDetail(Order order, Product product, int quantity, double unitPrice) {
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

    public OrderDetailDTO transformToDto() {
        return new OrderDetailDTO(this.getOrderDetailId(), this.getOrder(), this.getProduct(), this.getQuantity(),
                this.getUnitPrice());
    }
}
