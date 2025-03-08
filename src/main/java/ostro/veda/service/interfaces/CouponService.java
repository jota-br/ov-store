package ostro.veda.service.interfaces;

import ostro.veda.model.dto.CouponDto;

public interface CouponService extends Service<CouponDto> {

    String generateCode(String code);
}
