package ostro.veda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.model.dto.CouponDto;
import ostro.veda.repository.dao.Coupon;
import ostro.veda.repository.helpers.EntityManagerHelper;
import ostro.veda.repository.helpers.enums.CouponsColumns;
import ostro.veda.repository.interfaces.CouponRepository;
import ostro.veda.util.validation.ValidateParameter;

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
    public CouponDto add(CouponDto couponDTO) {

        log.info("add() new Coupon = {}", couponDTO.getCode());
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
    public CouponDto update(CouponDto couponDTO) {

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

    private Coupon buildCoupon(CouponDto couponDTO) {

        ValidateParameter.isNull(this.getClass(), couponDTO);

        return Coupon.builder()
                .code(couponDTO.getCode())
                .description(couponDTO.getDescription())
                .discountType(couponDTO.getDiscountType())
                .discountValue(couponDTO.getDiscountValue())
                .expirationDate(couponDTO.getExpirationDate())
                .usageLimit(couponDTO.getUsageLimit())
                .build();
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
