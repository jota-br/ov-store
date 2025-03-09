package ostro.veda.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ostro.veda.util.validation.ValidateCity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidateCity.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCity {
    String message() default "Invalid City";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
