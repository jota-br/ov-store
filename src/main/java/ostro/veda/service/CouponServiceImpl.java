package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.CouponDTO;
import ostro.veda.common.util.Action;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.CouponRepository;

import java.util.UUID;

@Slf4j
@Component
public class CouponServiceImpl implements CouponService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final CouponRepository couponRepository;

    public CouponServiceImpl(ApplicationEventPublisher applicationEventPublisher, CouponRepository couponRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponDTO add(@NonNull CouponDTO couponDTO) {
        try {
            log.info("add() new Coupon code = {}", couponDTO.getCode());
            ValidateUtil.validateCoupon(couponDTO);
            couponDTO = buildValidCoupon(couponDTO);
            couponDTO = couponRepository.add(couponDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, couponDTO, 1);

            return couponDTO;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public CouponDTO update(@NonNull CouponDTO couponDTO) {

        log.info("update() Coupon = {}", couponDTO.getCode());

        if (couponDTO.getUsageLimit() <= 0) {
            log.info("Coupon {} Usage Limit reached", couponDTO.getCode());
            return null;
        }

        couponDTO = couponRepository.update(couponDTO);

        this.auditCaller(applicationEventPublisher, this, Action.UPDATE, couponDTO, 1);
        return couponDTO;

    }

    @Override
    public String generateCode(String code) {
        final int MAX_CODE_LENGTH = 36;
        int length = code.length();
        final int FINAL_SIZE = MAX_CODE_LENGTH - length;
        return code + UUID.randomUUID().toString().substring(0, FINAL_SIZE);
    }

    private CouponDTO buildValidCoupon(CouponDTO couponDTO) {
        String code = generateCode(couponDTO.getCode());
        couponDTO = new CouponDTO(couponDTO.getCouponId(), code, couponDTO.getDescription(),
                couponDTO.getDiscountType(), couponDTO.getDiscountValue(), couponDTO.getExpirationDate(),
                couponDTO.getUsageLimit(), couponDTO.getCreatedAt(), couponDTO.getVersion());
        return couponDTO;
    }
}
