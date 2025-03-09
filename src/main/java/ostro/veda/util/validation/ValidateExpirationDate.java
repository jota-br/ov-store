package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.util.validation.annotation.ValidExpirationDate;

import java.time.LocalDateTime;

public class ValidateExpirationDate extends Validate implements ConstraintValidator<ValidExpirationDate, LocalDateTime> {

    private static final int MINIMUM_NUMBER_OF_HOURS_TO_EXPIRATION = 1;

    @Override
    public void initialize(ValidExpirationDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) return false;
        LocalDateTime minimumValidExpiration = LocalDateTime.now().plusHours(MINIMUM_NUMBER_OF_HOURS_TO_EXPIRATION);
        return localDateTime.isAfter(minimumValidExpiration) || localDateTime.equals(minimumValidExpiration);
    }
}
