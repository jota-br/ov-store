package ostro.veda.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ostro.veda.util.validation.ValidateId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidateId.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidId {
    String message() default "Invalid Id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
