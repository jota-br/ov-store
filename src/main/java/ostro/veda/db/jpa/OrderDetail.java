package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ostro.veda.common.dto.OrderDetailDTO;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private int orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    @Version
    private int version;

    public OrderDetail() {
    }

    public OrderDetailDTO transformToDto() {
        return new OrderDetailDTO(this.getOrderDetailId(), null,
                this.getProduct().transformToDto(), this.getQuantity(),
                this.getUnitPrice(), this.getVersion());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetail that = (OrderDetail) o;
        return orderDetailId == that.orderDetailId;
    }

    @Override
    public int hashCode() {
        return orderDetailId;
    }
}
