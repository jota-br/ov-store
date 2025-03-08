package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.common.dto.CouponDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.enums.CouponsColumns;
import ostro.veda.db.jpa.Coupon;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CouponRepositoryImpl implements CouponRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public CouponRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    @Transactional
    public CouponDTO add(@NonNull CouponDTO couponDTO) {

        log.info("add() new Coupon code = {}", couponDTO.getCode());
        List<Coupon> coupons = entityManagerHelper.findByFields(entityManager, Coupon.class, Map.of(
                CouponsColumns.CODE.getColumnName(), couponDTO.getCode())
        );

        try {
            if (coupons != null && !coupons.isEmpty()) return null;
            Coupon coupon = buildCoupon(couponDTO);

            this.entityManager.persist(coupon);
            return coupon.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public CouponDTO update(@NonNull CouponDTO couponDTO) {

        log.info("update() Coupon = {}", couponDTO.getCode());
        Coupon coupon = this.entityManager.find(Coupon.class, couponDTO.getCouponId());
        if (coupon == null) {
            log.warn("Invalid Coupon {}", couponDTO.toJSON());
            return null;
        }

        try {

            coupon.decreaseUsage();
            this.entityManager.persist(coupon);

            return coupon.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    @Override
    public Coupon buildCoupon(CouponDTO couponDTO) {
        return Coupon.builder()
                .code(couponDTO.getCode())
                .description(couponDTO.getDescription())
                .discountType(couponDTO.getDiscountType())
                .discountValue(couponDTO.getDiscountValue())
                .expirationDate(couponDTO.getExpirationDate())
                .usageLimit(couponDTO.getUsageLimit())
                .build();
    }
}
