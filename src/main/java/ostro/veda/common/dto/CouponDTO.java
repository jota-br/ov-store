package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class CouponDTO {

    private final int couponId;
    private final String code;
    private final String description;
    private final String discountType;
    private final double discountValue;
    private final LocalDateTime expirationDate;
    private final int usageLimit;
    private final LocalDateTime createdAt;
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"couponId\":" + couponId)
                .add("\"code\":\"" + code + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"discountType\":\"" + discountType + "\"")
                .add("\"discountValue\":" + discountValue)
                .add("\"expirationDate\":" + expirationDate)
                .add("\"usageLimit\":" + usageLimit)
                .add("\"createdAt\":" + createdAt)
                .toString();
    }
}
