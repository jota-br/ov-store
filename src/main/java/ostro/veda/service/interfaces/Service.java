package ostro.veda.service.interfaces;

import org.springframework.context.ApplicationEventPublisher;
import ostro.veda.util.enums.Action;
import ostro.veda.service.events.AuditEvent;

public interface Service<T> {

    T add(T t);

    T update(T t);

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
