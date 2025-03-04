package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.AuditDTO;
import ostro.veda.common.dto.AuditDataDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.AuditRepository;

@Slf4j
@Component
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public AuditDTO add(@NonNull AuditDataDTO auditDataDTO) {

        try {

            AuditDTO auditDTO = buildAuditDTO(auditDataDTO);
            ValidateUtil.validateAudit(auditDTO);
            auditDTO = SanitizeUtil.sanitizeAudit(auditDTO);
            return auditRepository.add(auditDTO);

        } catch (ErrorHandling.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public AuditDTO buildAuditDTO(AuditDataDTO auditDataDTO) {

        StringBuilder sb = new StringBuilder();

        String tableName = auditDataDTO.table();
        String action = auditDataDTO.action();
        String string = auditDataDTO.string();
        int id = auditDataDTO.id();
        int userId = auditDataDTO.userId();

        sb.append("Action = ")
                .append(action)
                .append(" -> Table = ")
                .append(tableName)
                .append(" : ID -> ")
                .append(id)
                .append(" : Changed by -> ")
                .append(userId)
                .append("\n")
                .append("\t")
                .append(string);


        return new AuditDTO(0, action, tableName, sb.toString(),
                null, null, userId);
    }

}

