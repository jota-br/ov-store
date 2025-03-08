package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableNames;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.ORDER_STATUS_HISTORY)
public class OrderStatusHistoryDto implements Dto {

    private final int orderStatusHistoryId;
    private final OrderDto order;
    private final String status;
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
