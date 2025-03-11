package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidStreetAddress;

public class ValidateStreetAddress extends Validate implements ConstraintValidator<ValidStreetAddress, String> {

    @Override
    public void initialize(ValidStreetAddress constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return string != null && !string.isBlank();
    }
}
