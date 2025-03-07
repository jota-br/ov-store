package ostro.veda.service;

import ostro.veda.common.dto.CouponDTO;

public interface CouponService extends Service<CouponDTO> {

    String generateCode(String code);
}
