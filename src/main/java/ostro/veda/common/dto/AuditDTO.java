package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AuditDTO {

    private final int auditId;
    private final String action;
    private final String changedTable;
    private final String changedData;
    private final LocalDateTime changedAt;
    private final UserDTO changedBy;
    private final int userId;
}
