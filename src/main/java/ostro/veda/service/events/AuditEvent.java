package ostro.veda.service.events;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import ostro.veda.common.util.Action;

@Slf4j
@Getter
public class AuditEvent extends ApplicationEvent {

    private final Action action;
    private final int userId;
    private final Object payload;

    @Builder
    public AuditEvent(Object source, @NonNull Action action, int userId, Object payload) {
        super(source);
        this.action = action;
        this.userId = userId;
        this.payload = payload;
    }
}
