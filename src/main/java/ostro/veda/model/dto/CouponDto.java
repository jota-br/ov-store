package ostro.veda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.annotation.MainService;
import ostro.veda.util.constant.ServiceName;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.enums.DiscountType;
import ostro.veda.util.validation.annotation.*;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.COUPON)
@MainService(getServiceClass = ServiceName.COUPON_SERVICE)
public class CouponDto implements Dto {

    private final int couponId;

    @NotBlank(message = "Code cannot be blank")
    @ValidCouponCode
    private final String code;

    @NotBlank(message = "Description cannot be blank")
    @ValidDescription
    private final String description;

    @NotNull(message = "Discount Type cannot be null")
    private final DiscountType discountType;

    @ValidDiscountValue
    private final double discountValue;

    @NotNull(message = "Expiration Date cannot be null")
    @ValidExpirationDate
    private final LocalDateTime expirationDate;

    @ValidCouponUsageLimit
    private final int usageLimit;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"couponId\":" + couponId)
                .add("\"code\":\"" + code + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"discountType\":" + discountType)
                .add("\"discountValue\":" + discountValue)
                .add("\"expirationDate\":\"" + expirationDate + "\"")
                .add("\"usageLimit\":" + usageLimit)
                .add("\"createdAt\":\"" + createdAt + "\"")
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }
}
