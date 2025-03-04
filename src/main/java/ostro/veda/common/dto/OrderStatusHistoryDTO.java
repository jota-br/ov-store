package main.java.ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderStatusHistoryDTO {

    private final int orderStatusHistoryId;
    private final OrderDTO order;
    private final String status;
    private final LocalDateTime changedAt;
    private final int version;
}
