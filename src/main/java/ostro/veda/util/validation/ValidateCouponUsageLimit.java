package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidCouponUsageLimit;

public class ValidateCouponUsageLimit extends Validate implements ConstraintValidator<ValidCouponUsageLimit, Integer> {

    public static final int HAS_NO_LIMIT_VALUE = -1;
    public static final int HAS_LIMIT_MINIMUM_VALUE = 1;

    @Override
    public void initialize(ValidCouponUsageLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer >= HAS_LIMIT_MINIMUM_VALUE || integer == HAS_NO_LIMIT_VALUE;
    }
}
