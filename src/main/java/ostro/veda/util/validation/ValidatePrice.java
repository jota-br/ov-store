package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidPrice;

public class ValidatePrice implements ConstraintValidator<ValidPrice, Double> {

    public static final double MINIMUM_VALID_PRICE = 0;

    @Override
    public void initialize(ValidPrice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double doubleValue, ConstraintValidatorContext constraintValidatorContext) {
        return doubleValue >= MINIMUM_VALID_PRICE;
    }
}
