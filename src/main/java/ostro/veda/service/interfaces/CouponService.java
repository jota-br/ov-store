package ostro.veda.service.interfaces;

import lombok.NonNull;
import ostro.veda.model.dto.CouponDto;

import java.util.UUID;

public interface CouponService extends Service<CouponDto> {

    private String generateCode(@NonNull String code) {
        final int MAX_CODE_LENGTH = 16;
        int length = code.length();
        final int FINAL_SIZE = MAX_CODE_LENGTH - length;
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(), 0, FINAL_SIZE);
        sb.insert(0, code);
        return sb.toString();
    }

    default CouponDto buildValidCoupon(@NonNull CouponDto couponDTO) {
        String code = generateCode(couponDTO.getCode());
        couponDTO = new CouponDto(couponDTO.getCouponId(), code, couponDTO.getDescription(),
                couponDTO.getDiscountType(), couponDTO.getDiscountValue(), couponDTO.getExpirationDate(),
                couponDTO.getUsageLimit(), couponDTO.getCreatedAt(), couponDTO.getUpdatedAt(), couponDTO.getVersion());
        return couponDTO;
    }
}
