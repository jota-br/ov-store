package ostro.veda.service;


import ostro.veda.common.dto.AuditDTO;
import ostro.veda.common.dto.AuditDataDTO;

public interface AuditService {

    AuditDTO add(AuditDataDTO auditDataDTO);
    AuditDTO buildAuditDTO(AuditDataDTO auditDataDTO);
}
