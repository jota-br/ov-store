package ostro.veda.db;

import ostro.veda.common.dto.CouponDTO;
import ostro.veda.db.jpa.Coupon;

public interface CouponRepository extends Repository<CouponDTO> {

    Coupon buildCoupon(CouponDTO couponDTO);
}
