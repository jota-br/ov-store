package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidTotalAmount;

public class ValidateTotalAmount implements ConstraintValidator<ValidTotalAmount, Double> {

    public static final double MINIMUM_VALID_TOTAL_AMOUNT = 0;

    @Override
    public void initialize(ValidTotalAmount constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double doubleValue, ConstraintValidatorContext constraintValidatorContext) {
        return doubleValue >= MINIMUM_VALID_TOTAL_AMOUNT;
    }
}
