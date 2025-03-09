package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidUsername;

import java.util.regex.Pattern;

public class ValidateUsername implements ConstraintValidator<ValidUsername, String> {

    private static final String NAME_PATTERN = "^[a-zA-Z0-9@_-]{8,20}$";
    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.isBlank()) return false;
        return PATTERN.matcher(string).matches();
    }
}
