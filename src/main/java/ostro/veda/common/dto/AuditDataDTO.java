package main.java.ostro.veda.common.dto;

public record AuditDataDTO(String string, int integer, double doubleValue,
                           String columnName, String action, String table, int id, int userId){}
