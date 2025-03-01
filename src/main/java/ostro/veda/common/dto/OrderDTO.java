package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDTO {

    private final int orderId;
    private final int userId;
    private final LocalDateTime orderDate;
    private final double totalAmount;
    private final String status;
    private final List<OrderDetailDTO> orderDetails;
    private final AddressDTO shippingAddress;
    private final AddressDTO billingAddress;
    private final List<OrderStatusHistoryDTO> orderStatusHistory;
    private final LocalDateTime updatedAt;
    private final int version;
}
