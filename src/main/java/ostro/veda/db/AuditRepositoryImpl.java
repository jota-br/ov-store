package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.common.dto.AuditDTO;
import ostro.veda.db.jpa.Audit;
import ostro.veda.db.jpa.User;

@Slf4j
@Component
public class AuditRepositoryImpl implements AuditRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public AuditDTO add(@NonNull AuditDTO auditDTO) {

        User user = this.entityManager.find(User.class, auditDTO.getChangedBy().getUserId());
        if (user == null) return null;

        try {

            Audit audit = buildAudit(auditDTO, user);
            this.entityManager.persist(audit);
            return audit.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public Audit buildAudit(AuditDTO auditDTO, User user) {
        return new Audit()
                .setAuditId(auditDTO.getAuditId())
                .setAction(auditDTO.getAction())
                .setChangedTable(auditDTO.getChangedTable())
                .setChangedData(auditDTO.getChangedData())
                .setChangedBy(user);
    }
}
