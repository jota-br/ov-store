package ostro.veda.util.validation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidateParameter {

    public static <T> void isNull(Class<T> entity, Object... list) throws NullPointerException {
        for (Object obj : list) {
            if (obj == null) {
                String message = "Null parameters -> " + entity.getPackageName() + "." + entity.getSimpleName();
                log.warn(message);
                throw new NullPointerException(message);
            }
        }
    }

}
