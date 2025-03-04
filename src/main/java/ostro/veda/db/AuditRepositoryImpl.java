package main.java.ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.AuditDTO;
import main.java.ostro.veda.db.jpa.Audit;
import main.java.ostro.veda.db.jpa.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AuditRepositoryImpl implements AuditRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<AuditDTO> add(List<AuditDTO> auditDTOS) {

        User user = this.entityManager.find(User.class, auditDTOS.get(0).getUserId());
        if (user == null) return null;

        try {

            List<AuditDTO> auditDTOList = new ArrayList<>();
            List<Audit> audits = buildAudit(auditDTOS, user);

            for (Audit audit : audits) {

                this.entityManager.persist(audit);
                auditDTOList.add(audit.transformToDto());

            }

            return auditDTOList;
        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public List<Audit> buildAudit(List<AuditDTO> auditDTOS, User user) {

        int userId = user.getUserId();
        List<Audit> auditList = new ArrayList<>();
        for (AuditDTO auditDTO : auditDTOS) {

            if (userId != auditDTO.getUserId()) return null;
            auditList.add(new Audit()
                    .setAuditId(auditDTO.getAuditId())
                    .setAction(auditDTO.getAction())
                    .setChangedTable(auditDTO.getChangedTable())
                    .setChangedData(auditDTO.getChangedData())
                    .setChangedBy(user));

        }
        return auditList;
    }
}
