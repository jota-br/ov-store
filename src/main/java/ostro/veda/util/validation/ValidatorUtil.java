package ostro.veda.util.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.util.exception.InputException;

import java.util.stream.Collectors;

@Slf4j
@Component
public class ValidatorUtil {

    @Autowired
    private Validator validator;

    public <T> void validate(@Valid T obj) throws InputException.InvalidInputException {

        log.info("Validating = {}", obj.toString());
        var violations = validator.validate(obj);
        if (violations.isEmpty()) {
            log.info("Object is valid");
            return;
        }

        log.warn("Object is not valid");
        String violationsString = violations.stream()
                .peek(violation -> log.info(violation.getMessage()))
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" : "));
        throw new InputException.InvalidInputException(violationsString, obj.toString());
    }
}
