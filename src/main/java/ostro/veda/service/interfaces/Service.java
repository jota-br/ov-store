package ostro.veda.service.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import ostro.veda.util.enums.Action;
import ostro.veda.service.events.AuditEvent;

@Validated
public interface Service<T> {

    T add(@NotNull @Valid T t);

    T update(@NotNull @Valid T t);

    default void auditCaller(ApplicationEventPublisher applicationEventPublisher, Service<T> entity, Action action, T payload, int userId) {
        AuditEvent event = AuditEvent.builder()
                .source(entity)
                .action(action)
                .payload(payload)
                .userId(userId)
                .build();
        applicationEventPublisher.publishEvent(event);
    }
}
