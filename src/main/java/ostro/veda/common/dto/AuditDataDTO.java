package ostro.veda.common.dto;

public record AuditDataDTO(String string, String action, String table, int id, int userId){}
