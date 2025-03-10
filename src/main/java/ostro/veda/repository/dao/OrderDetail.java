package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ostro.veda.model.dto.OrderDetailDto;

@Getter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private int orderDetailId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Setter
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    @Version
    private int version;

    public OrderDetail() {
    }

    public OrderDetailDto transformToDto() {
        return new OrderDetailDto(this.getOrderDetailId(), null,
                this.getProduct().transformToDto(), this.getQuantity(),
                this.getUnitPrice(), this.getVersion());
    }
}
