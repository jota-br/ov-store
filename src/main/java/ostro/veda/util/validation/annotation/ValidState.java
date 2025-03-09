package ostro.veda.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ostro.veda.util.validation.ValidateState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidateState.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidState {
    String message() default "Invalid State";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
