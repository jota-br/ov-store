package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidPassword;

import java.util.regex.Pattern;

public class ValidatePassword extends Validate implements ConstraintValidator<ValidPassword, String> {

    private static final String NAME_PATTERN = "^[A-Za-z0-9!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/|\\\\]{8,20}$";
    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.isBlank()) return false;
        return PATTERN.matcher(string).matches();
    }
}
