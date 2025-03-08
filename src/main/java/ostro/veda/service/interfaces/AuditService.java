package ostro.veda.service.interfaces;


import ostro.veda.model.dto.AuditDto;
import ostro.veda.model.dto.AuditDataDto;

public interface AuditService {

    AuditDto add(AuditDataDto auditDataDTO);
    AuditDto buildAuditDTO(AuditDataDto auditDataDTO);
}
