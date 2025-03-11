package ostro.veda.service.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ostro.veda.model.dto.CouponDto;
import ostro.veda.util.validation.annotation.ValidCouponCode;

import java.util.UUID;

public interface CouponService extends Service<CouponDto> {

    private String generateCode(@NotNull @ValidCouponCode String code) {
        final int MAX_CODE_LENGTH = 16;
        int length = code.length();
        final int FINAL_SIZE = MAX_CODE_LENGTH - length;
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(), 0, FINAL_SIZE);
        sb.insert(0, code);
        return sb.toString();
    }

    default CouponDto buildValidCoupon(@NotNull @Valid CouponDto couponDTO) {
        String code = generateCode(couponDTO.getCode());
        couponDTO = new CouponDto(couponDTO.getCouponId(), code, couponDTO.getDescription(),
                couponDTO.getDiscountType(), couponDTO.getDiscountValue(), couponDTO.getExpirationDate(),
                couponDTO.getUsageLimit(), couponDTO.getCreatedAt(), couponDTO.getUpdatedAt(), couponDTO.getVersion());
        return couponDTO;
    }
}
