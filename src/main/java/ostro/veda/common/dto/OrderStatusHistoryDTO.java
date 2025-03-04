package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class OrderStatusHistoryDTO {

    private final int orderStatusHistoryId;
    private final OrderDTO order;
    private final String status;
    private final LocalDateTime changedAt;
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"orderStatusHistoryId\":" + orderStatusHistoryId)
                .add("\"order\":" + order.getOrderId())
                .add("\"status\":\"" + status + "\"")
                .add("\"changedAt\":" + changedAt)
                .toString();
    }
}
