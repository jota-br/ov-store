package ostro.veda.repository.interfaces;

import ostro.veda.model.dto.AuditDto;
import ostro.veda.repository.dao.Audit;
import ostro.veda.repository.dao.User;

public interface AuditRepository {

    AuditDto add(AuditDto auditDTO);

    Audit buildAudit(AuditDto auditDTO, User user);
}
