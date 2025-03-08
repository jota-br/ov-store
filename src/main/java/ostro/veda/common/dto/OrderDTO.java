package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ostro.veda.common.util.Auditable;
import ostro.veda.common.util.TableNames;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.ORDER)
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
    private final CouponDTO coupon;
    private final LocalDateTime updatedAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"orderId\":" + orderId)
                .add("\"userId\":" + userId)
                .add("\"orderDate\":\"" + orderDate + "\"")
                .add("\"totalAmount\":" + totalAmount)
                .add("\"status\":\"" + status + "\"")
                .add("\"orderDetails\":" + orderDetails)
                .add("\"shippingAddress\":" + shippingAddress)
                .add("\"billingAddress\":" + billingAddress)
                .add("\"orderStatusHistory\":" + orderStatusHistory)
                .add("\"coupon\":" + coupon)
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }
}
