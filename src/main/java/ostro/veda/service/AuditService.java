package main.java.ostro.veda.service;


import main.java.ostro.veda.common.dto.AuditDTO;
import main.java.ostro.veda.common.dto.AuditDataDTO;

import java.util.List;

public interface AuditService {

    List<AuditDTO> add(List<AuditDataDTO> auditDataDTO);
    List<AuditDTO> buildAuditDTO(List<AuditDataDTO> auditDataDTO);
}
