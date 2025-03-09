package ostro.veda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.model.dto.AuditDto;
import ostro.veda.repository.dao.Audit;
import ostro.veda.repository.dao.User;
import ostro.veda.repository.interfaces.AuditRepository;
import ostro.veda.util.validation.ValidateParameter;

@Slf4j
@Component
public class AuditRepositoryImpl implements AuditRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public AuditDto add(AuditDto auditDTO) {

        log.info("add() new Audit = {}", auditDTO.getAction());
        User user = this.entityManager.find(User.class, auditDTO.getUserId());
        if (user == null) {
            log.warn("add() AuditRepository with null user, a new user is being created or an invalid userId has been provided");
            return null;
        }

        try {

            Audit audit = buildAudit(auditDTO, user);
            this.entityManager.persist(audit);
            return audit.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    private Audit buildAudit(AuditDto auditDTO, User user) {

        ValidateParameter.isNull(this.getClass(), auditDTO, user);

        log.info("buildAudit()");
        int userId = user.getUserId();
        if (userId != auditDTO.getUserId()) return null;

        return Audit.builder()
                .auditId(auditDTO.getAuditId())
                .action(auditDTO.getAction())
                .changedTable(auditDTO.getChangedTable())
                .changedData(auditDTO.getChangedData())
                .changedBy(user)
                .build();
    }
}
