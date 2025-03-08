package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.service.interfaces.AuditService;
import ostro.veda.model.dto.AuditDto;
import ostro.veda.model.dto.AuditDataDto;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.validation.ValidateUtil;
import ostro.veda.repository.interfaces.AuditRepository;

@Slf4j
@Component
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public AuditDto add(@NonNull AuditDataDto auditDataDTO) {

        try {
            log.info("add() new Audit = {} -> {}", auditDataDTO.action(), auditDataDTO.table());
            AuditDto auditDTO = buildAuditDTO(auditDataDTO);
            ValidateUtil.validateAudit(auditDTO);
//            auditDTO = SanitizeUtil.sanitizeAudit(auditDTO);
            return auditRepository.add(auditDTO);

        } catch (InputException.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public AuditDto buildAuditDTO(AuditDataDto auditDataDTO) {

        log.info("buildAuditDTO()");
        String tableName = auditDataDTO.table();
        String action = auditDataDTO.action();
        String string = auditDataDTO.string();
        int userId = auditDataDTO.userId();

        return new AuditDto(0, action, tableName, string,
                null, null, userId);
    }

}

