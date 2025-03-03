package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.AuditDTO;
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
    public AuditDTO add(@NonNull AuditDTO auditDTO) {

        try {

            ValidateUtil.validateAudit(auditDTO);
            auditDTO = SanitizeUtil.sanitizeAudit(auditDTO);
            return auditRepository.add(auditDTO);

        } catch (ErrorHandling.InvalidInputException e) {

            log.warn(e.getMessage());
            return null;

        }
    }
}
