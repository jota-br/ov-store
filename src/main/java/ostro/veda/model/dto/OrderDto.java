package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.annotation.MainService;
import ostro.veda.util.constant.MainServiceNames;
import ostro.veda.util.constant.TableNames;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.ORDER)
@MainService(getServiceClass = MainServiceNames.ORDER_SERVICE)
public class OrderDto implements Dto {

    private final int orderId;
    private final int userId;
    private final LocalDateTime orderDate;
    private final double totalAmount;
    private final String status;

    @Setter
    private List<OrderDetailDto> orderDetails;

    private final AddressDto shippingAddress;
    private final AddressDto billingAddress;
    private final List<OrderStatusHistoryDto> orderStatusHistory;
    private final CouponDto coupon;
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
