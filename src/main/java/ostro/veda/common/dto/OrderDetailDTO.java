package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class OrderDetailDTO {

    private final int orderDetailId;
    private final OrderDTO order;
    private final ProductDTO product;
    private final int quantity;
    private final double unitPrice;
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"orderDetailId\":" + orderDetailId)
                .add("\"order\":" + order.getOrderId())
                .add("\"product\":" + product)
                .add("\"quantity\":" + quantity)
                .add("\"unitPrice\":" + unitPrice)
                .toString();
    }
}
