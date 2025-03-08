package ostro.veda.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType {

    PERCENTAGE("percentage"),
    AMOUNT("amount");

    private final String discountType;
}
