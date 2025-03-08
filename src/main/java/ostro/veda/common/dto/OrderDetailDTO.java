package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.common.util.Auditable;
import ostro.veda.common.util.TableNames;

import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.ORDER_DETAIL)
public class OrderDetailDTO {

    private final int orderDetailId;
    private final OrderDTO order;
    private final ProductDTO product;
    private final int quantity;
    private final double unitPrice;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"orderDetailId\":" + orderDetailId)
                .add("\"product\":" + product)
                .add("\"quantity\":" + quantity)
                .add("\"unitPrice\":" + unitPrice)
                .toString();
    }
}
