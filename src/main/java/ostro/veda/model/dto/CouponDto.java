package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.annotation.MainService;
import ostro.veda.util.constant.MainServiceNames;
import ostro.veda.util.constant.TableNames;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.COUPON)
@MainService(getServiceClass = MainServiceNames.COUPON_SERVICE)
public class CouponDto implements Dto {

    private final int couponId;
    private final String code;
    private final String description;
    private final String discountType;
    private final double discountValue;
    private final LocalDateTime expirationDate;
    private final int usageLimit;
    private final LocalDateTime createdAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"couponId\":" + couponId)
                .add("\"code\":\"" + code + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"discountType\":\"" + discountType + "\"")
                .add("\"discountValue\":" + discountValue)
                .add("\"expirationDate\":\"" + expirationDate + "\"")
                .add("\"usageLimit\":" + usageLimit)
                .add("\"createdAt\":\"" + createdAt + "\"")
                .toString();
    }
}
