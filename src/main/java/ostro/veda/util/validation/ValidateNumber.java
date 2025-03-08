package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidNumber;

public class ValidateNumber implements ConstraintValidator<ValidNumber, Integer> {

    public static final int MINIMUM_VALID_NUMBER = 0;

    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer >= MINIMUM_VALID_NUMBER;
    }
}
