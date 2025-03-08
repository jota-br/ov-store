package ostro.veda.repository.interfaces;

import ostro.veda.model.dto.CouponDto;
import ostro.veda.repository.dao.Coupon;

public interface CouponRepository extends Repository<CouponDto> {

    Coupon buildCoupon(CouponDto couponDTO);
}
