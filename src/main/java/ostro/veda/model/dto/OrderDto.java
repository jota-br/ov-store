package ostro.veda.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.annotation.MainService;
import ostro.veda.util.constant.ServiceName;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.enums.OrderStatus;
import ostro.veda.util.validation.annotation.ValidTotalAmount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.ORDER)
@MainService(getServiceClass = ServiceName.ORDER_SERVICE)
public class OrderDto implements Dto {

    private final int orderId;
    private final int userId;
    private final LocalDateTime orderDate;

    @ValidTotalAmount
    private final double totalAmount;

    @NotNull(message = "Order Status cannot be null")
    private final OrderStatus status;

    @Setter
    @NotNull(message = "Order Detail cannot be null")
    @Valid
    @Size(min = 1, message = "Order must contain at least one Detail")
    private List<OrderDetailDto> orderDetails;

    @NotNull(message = "Shipping Address cannot be null")
    private final AddressDto shippingAddress;

    @NotNull(message = "Billing Address cannot be null")
    private final AddressDto billingAddress;

    @Valid
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
                .add("\"status\":\"" + status.getStatus() + "\"")
                .add("\"orderDetails\":" + orderDetails)
                .add("\"shippingAddress\":" + shippingAddress)
                .add("\"billingAddress\":" + billingAddress)
                .add("\"orderStatusHistory\":" + orderStatusHistory)
                .add("\"coupon\":" + coupon)
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }
}
