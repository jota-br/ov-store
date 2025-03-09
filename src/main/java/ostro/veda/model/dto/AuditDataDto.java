package ostro.veda.model.dto;

import ostro.veda.util.enums.Action;

public record AuditDataDto(String string, Action action, String table, int userId){}
