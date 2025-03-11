package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.CouponDto;
import ostro.veda.repository.interfaces.CouponRepository;
import ostro.veda.service.interfaces.CouponService;
import ostro.veda.util.enums.Action;

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
    public CouponDto add(CouponDto couponDTO) {

        try {

            log.info("add() new Coupon code = {}", couponDTO.getCode());

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
    public CouponDto update(CouponDto couponDTO) {

        try {

            log.info("update() Coupon = {}", couponDTO.getCode());

            if (couponDTO.getUsageLimit() <= 0) {
                log.info("Coupon {} Usage Limit reached", couponDTO.getCode());
                return null;
            }

            couponDTO = couponRepository.update(couponDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, couponDTO, 1);
            return couponDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }
}
