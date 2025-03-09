package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidDescription;

import java.util.regex.Pattern;

public class ValidateDescription implements ConstraintValidator<ValidDescription, String> {

    private static final String NAME_PATTERN = "^[a-zA-Z\\s\\p{Punct}0-9\n]{0,510}$";
    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);

    @Override
    public void initialize(ValidDescription constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.isBlank()) return false;
        return PATTERN.matcher(string).matches();
    }
}
