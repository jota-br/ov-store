package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableNames;

import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.ORDER_DETAIL)
public class OrderDetailDto implements Dto {

    private final int orderDetailId;
    private final OrderDto order;
    private final ProductDto product;
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
