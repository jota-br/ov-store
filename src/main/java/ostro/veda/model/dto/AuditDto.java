package ostro.veda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.util.enums.Action;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AuditDto {

    private final int auditId;

    @NotNull(message = "Action cannot be null")
    private final Action action;

    @NotBlank(message = "Changed Table cannot be blank")
    private final String changedTable;

    @NotBlank(message = "Changed Data cannot be blank")
    private final String changedData;

    private final LocalDateTime changedAt;

    @NotNull(message = "Changed By cannot be null")
    private final UserDto changedBy;

    private final int userId;
}
