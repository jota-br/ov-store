package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.EmailValidator;
import ostro.veda.util.validation.annotation.ValidEmail;

public class ValidateEmail implements ConstraintValidator<ValidEmail, String> {

    EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.isBlank()) return false;
        return emailValidator.isValid(string);
    }
}
