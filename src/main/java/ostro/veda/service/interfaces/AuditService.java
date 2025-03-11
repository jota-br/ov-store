package ostro.veda.service.interfaces;


import jakarta.validation.constraints.NotNull;
import ostro.veda.model.dto.AuditDataDto;
import ostro.veda.model.dto.AuditDto;

public interface AuditService {

    AuditDto add(@NotNull AuditDataDto auditDataDTO);
    AuditDto buildAuditDTO(@NotNull AuditDataDto auditDataDTO);
}
