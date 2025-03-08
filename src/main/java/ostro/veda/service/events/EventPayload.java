package ostro.veda.service.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import ostro.veda.common.dto.Dto;

@Slf4j
@Getter
public class EventPayload extends ApplicationEvent {

    private final Dto dto;
    private final String methodName;

    public EventPayload(@NonNull Object source, @NonNull Dto dto, @NonNull String methodName) {
        super(source);
        this.dto = dto;
        this.methodName = methodName;
    }
}
