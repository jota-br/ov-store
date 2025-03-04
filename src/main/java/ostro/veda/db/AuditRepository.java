package main.java.ostro.veda.db;

import main.java.ostro.veda.common.dto.AuditDTO;
import main.java.ostro.veda.db.jpa.Audit;
import main.java.ostro.veda.db.jpa.User;

import java.util.List;

public interface AuditRepository {

    List<AuditDTO> add(List<AuditDTO> auditDTOS);

    List<Audit> buildAudit(List<AuditDTO> auditDTOS, User user);
}
