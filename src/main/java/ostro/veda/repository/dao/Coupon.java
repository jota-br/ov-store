package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.model.dto.CouponDto;
import ostro.veda.util.enums.DiscountType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", length = 50, nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false)
    private double discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "usage_limit", nullable = false)
    private int usageLimit;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    public Coupon() {
    }

    public Coupon decreaseUsage() {
        this.usageLimit -= 1;
        return this;
    }

    public CouponDto transformToDto() {
        return new CouponDto(this.getCouponId(), this.getCode(), this.getDescription(), this.getDiscountType(),
                this.getDiscountValue(), this.getExpirationDate(), this.getUsageLimit(), this.getCreatedAt(),
                this.getUpdatedAt(), this.getVersion());
    }
}
