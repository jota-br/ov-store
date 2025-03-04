package main.java.ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.AuditDTO;
import main.java.ostro.veda.common.dto.AuditDataDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.common.validation.SanitizeUtil;
import main.java.ostro.veda.common.validation.ValidateUtil;
import main.java.ostro.veda.db.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public List<AuditDTO> add(@NonNull List<AuditDataDTO> auditDataDTO) {

        try {

            List<AuditDTO> auditDTOS = buildAuditDTO(auditDataDTO);
            ValidateUtil.validateAudit(auditDTOS);
            auditDTOS = SanitizeUtil.sanitizeAudit(auditDTOS);
            return auditRepository.add(auditDTOS);

        } catch (ErrorHandling.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public List<AuditDTO> buildAuditDTO(List<AuditDataDTO> auditDataDTO) {

        List<AuditDTO> auditDTOS = new ArrayList<>();

        for (AuditDataDTO data : auditDataDTO) {
            StringBuilder sb = new StringBuilder();

            String tableName = data.table();
            String action = data.action();
            int id = data.id();

            sb.append("Action = ")
                    .append(action)
                    .append(" -> Table = ")
                    .append(tableName)
                    .append(" : ID -> ")
                    .append(id == 0 ? "new" : id)
                    .append("\n");

            String string = data.string();
            int integer = data.integer();
            double doubleValue = data.doubleValue();
            String columnName = data.columnName();
            int userId = data.userId();

                sb.append("\t")
                        .append("Column -> ")
                        .append(columnName)
                        .append("\n")
                        .append(" -> changed to = ")
                        .append("\n")
                        .append("{")
                        .append(string)
                        .append(", ")
                        .append(integer)
                        .append(", ")
                        .append(doubleValue)
                        .append("}")
                        .append("\n");

            auditDTOS.add(new AuditDTO(0, action, tableName, sb.toString(),
                    null, null, userId));
        }
        return auditDTOS;
    }
}

