package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class OrderDTO {

    private final int orderId;
    private final int userId;
    private final LocalDateTime orderDate;
    private final double totalAmount;
    private final String status;

    @Setter
    private List<OrderDetailDTO> orderDetails;

    private final AddressDTO shippingAddress;
    private final AddressDTO billingAddress;
    private final List<OrderStatusHistoryDTO> orderStatusHistory;
    private final LocalDateTime updatedAt;
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"orderId\":" + orderId)
                .add("\"userId\":" + userId)
                .add("\"orderDate\":" + orderDate)
                .add("\"totalAmount\":" + totalAmount)
                .add("\"status\":\"" + status + "\"")
                .add("\"orderDetails\":" + orderDetails)
                .add("\"shippingAddress\":" + shippingAddress)
                .add("\"billingAddress\":" + billingAddress)
                .add("\"orderStatusHistory\":" + orderStatusHistory)
                .add("\"updatedAt\":" + updatedAt)
                .toString();
    }
}
