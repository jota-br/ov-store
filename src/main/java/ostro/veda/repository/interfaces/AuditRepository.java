package ostro.veda.repository.interfaces;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ostro.veda.model.dto.AuditDto;

@Validated
public interface AuditRepository {

    AuditDto add(@NotNull AuditDto auditDTO);
}
