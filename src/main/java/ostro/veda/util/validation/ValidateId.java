package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidId;

public class ValidateId extends Validate implements ConstraintValidator<ValidId, Integer> {

    public static final int VALID_ID = 1;

    @Override
    public void initialize(ValidId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer >= VALID_ID;
    }
}
