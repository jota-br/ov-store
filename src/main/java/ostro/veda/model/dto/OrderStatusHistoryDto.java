package ostro.veda.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.ORDER_STATUS_HISTORY)
public class OrderStatusHistoryDto implements Dto {

    private final int orderStatusHistoryId;

    @NotNull(message = "Order cannot be null")
    private final OrderDto order;

    @NotNull(message = "Order Status cannot be null")
    private final OrderStatus status;

    private final LocalDateTime changedAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"orderStatusHistoryId\":" + orderStatusHistoryId)
//                .add("\"order\":" + order.getOrderId())
                .add("\"status\":\"" + status + "\"")
                .add("\"changedAt\":\"" + changedAt + "\"")
                .toString();
    }
}
