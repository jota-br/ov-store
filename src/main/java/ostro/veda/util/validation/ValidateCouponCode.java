package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidCouponCode;

import java.util.regex.Pattern;

public class ValidateCouponCode extends Validate implements ConstraintValidator<ValidCouponCode, String> {

    private static final String NAME_PATTERN = "^[A-Z\\-0-9]{3,50}$";
    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);

    @Override
    public void initialize(ValidCouponCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.isBlank()) return false;
        return PATTERN.matcher(string).matches();
    }
}
