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

    public <T> void validation(@Valid T dto) throws InputException.InvalidInputException {
        var violations = validator.validate(dto);
        if (violations.isEmpty()) return;

        String violationsString = violations.stream()
                .peek(violation -> log.info(violation.getMessage()))
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" : "));
        throw new InputException.InvalidInputException(violationsString, dto.toString());
    }
}
