package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidStock;

public class ValidateStock extends Validate implements ConstraintValidator<ValidStock, Integer> {

    public static final int MINIMUM_VALID_NUMBER = 0;

    @Override
    public void initialize(ValidStock constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer >= MINIMUM_VALID_NUMBER;
    }
}
