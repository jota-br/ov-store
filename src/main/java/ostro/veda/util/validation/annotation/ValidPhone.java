package ostro.veda.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ostro.veda.util.validation.ValidatePhone;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidatePhone.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Invalid Phone";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
