package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import ostro.veda.common.dto.CouponDTO;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private int couponId;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_type", length = 50, nullable = false)
    private String discountType;

    @Column(name = "discount_value", nullable = false)
    private double discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "usage_limit", nullable = false)
    private int usageLimit;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Version
    private int version;

    public Coupon() {
    }

    public Coupon decreaseUsage() {
        this.usageLimit -= 1;
        return this;
    }

    public CouponDTO transformToDto() {
        return new CouponDTO(this.getCouponId(), this.getCode(), this.getDescription(), this.getDiscountType(),
                this.getDiscountValue(), this.getExpirationDate(), this.getUsageLimit(), this.getCreatedAt(),
                this.getVersion());
    }
}
