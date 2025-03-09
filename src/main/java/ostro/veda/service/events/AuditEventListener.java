package ostro.veda.service.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.AuditDataDto;
import ostro.veda.util.enums.Action;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.service.interfaces.AuditService;

@Slf4j
@Component
public class AuditEventListener {

    private final AuditService auditServiceImpl;

    @Autowired
    public AuditEventListener(AuditService auditServiceImpl) {
        this.auditServiceImpl = auditServiceImpl;
    }

    @EventListener
    public void handleAuditEvent(AuditEvent auditEvent) {
        log.info("handleAuditEvent() received");

        Object payload = auditEvent.getPayload();
        var source = auditEvent.getSource();
        Auditable annotation = payload.getClass().getAnnotation(Auditable.class);
        if (annotation == null) {

            log.info("handleAuditEvent() -> No listening class received, event will close");
            return;

        }

        log.info("handleAuditEvent() -> found class {}", payload.getClass().getSimpleName());
        String table = annotation.tableName();
        String string = payload.toString();
        Action action = auditEvent.getAction();

        AuditDataDto auditDataDTO = new AuditDataDto(string, action, table, auditEvent.getUserId());
        auditServiceImpl.add(auditDataDTO);
    }
}
