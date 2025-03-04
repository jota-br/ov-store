package ostro.veda.db;

import ostro.veda.common.dto.AuditDTO;
import ostro.veda.db.jpa.Audit;
import ostro.veda.db.jpa.User;

public interface AuditRepository {

    AuditDTO add(AuditDTO auditDTO);

    Audit buildAudit(AuditDTO auditDTO, User user);
}
