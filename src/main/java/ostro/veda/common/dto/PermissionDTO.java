package ostro.veda.common.dto;

import java.time.LocalDateTime;

public class PermissionDTO {

    private final int permissionId;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PermissionDTO(int permissionId, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.permissionId = permissionId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
