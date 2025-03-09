package ostro.veda.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ostro.veda.model.dto.UserDto;
import ostro.veda.util.validation.annotation.ValidUser;

public class ValidateUser implements ConstraintValidator<ValidUser, UserDto> {

    @Override
    public void initialize(ValidUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDto dto, ConstraintValidatorContext constraintValidatorContext) {
        return dto != null;
    }
}
