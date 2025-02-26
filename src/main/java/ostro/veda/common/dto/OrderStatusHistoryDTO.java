package ostro.veda.common.dto;

import java.time.LocalDateTime;

public class OrderStatusHistoryDTO {

    private final int orderStatusHistoryId;
    private final OrderDTO order;
    private final String status;
    private final LocalDateTime changedAt;

    public OrderStatusHistoryDTO(int orderStatusHistoryId, OrderDTO order, String status, LocalDateTime changedAt) {
        this.orderStatusHistoryId = orderStatusHistoryId;
        this.order = order;
        this.status = status;
        this.changedAt = changedAt;
    }

    public OrderStatusHistoryDTO(OrderDTO order, String status) {
        this(-1, order, status, null);
    }

    public int getOrderStatusHistoryId() {
        return orderStatusHistoryId;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }
}
