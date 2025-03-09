package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidPhone;

import java.util.regex.Pattern;

public class ValidatePhone extends Validate implements ConstraintValidator<ValidPhone, String> {

    private static final String NAME_PATTERN = "\\+\\d{6,14}";
    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.isBlank()) return false;
        return PATTERN.matcher(string).matches();
    }
}
