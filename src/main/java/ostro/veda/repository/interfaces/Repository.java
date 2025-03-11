package ostro.veda.repository.interfaces;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface Repository<T> extends AutoCloseable {

    T add(@NotNull T t);

    T update(@NotNull T t);
}
