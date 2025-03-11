package ostro.veda.util.validation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Validate {

    public Validate() {
        log(this);
    }

    protected <T> void log(T t) {
        log.info("Validating = {}", t.getClass().getSimpleName());
    }
}
