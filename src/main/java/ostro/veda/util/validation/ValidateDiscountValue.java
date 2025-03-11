package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidDiscountValue;

public class ValidateDiscountValue extends Validate implements ConstraintValidator<ValidDiscountValue, Double> {

    public static final double MINIMUM_VALID_VALUE = 1;

    @Override
    public void initialize(ValidDiscountValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double doubleValue, ConstraintValidatorContext constraintValidatorContext) {
        return doubleValue >= MINIMUM_VALID_VALUE;
    }
}
