package ostro.veda.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.validation.annotation.ValidNumber;
import ostro.veda.util.validation.annotation.ValidPrice;

import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.ORDER_DETAIL)
public class OrderDetailDto implements Dto {

    private final int orderDetailId;

    @NotNull(message = "Order cannot be null")
    private final OrderDto order;

    @NotNull(message = "Product cannot be null")
    private final ProductDto product;

    @ValidNumber
    private final int quantity;

    @ValidPrice
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
