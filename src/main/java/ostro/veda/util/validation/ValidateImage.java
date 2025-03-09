package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidImage;

import java.util.regex.Pattern;

public class ValidateImage extends Validate implements ConstraintValidator<ValidImage, String> {

    private static final String NAME_PATTERN = "^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$";
    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);

    @Override
    public void initialize(ValidImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null || string.isBlank()) return false;
        return PATTERN.matcher(string).matches();
    }
}
