package ostro.veda.common.dto;

import ostro.veda.db.jpa.Permission;

import java.time.LocalDateTime;
import java.util.List;

public class RoleDTO {

    private final int roleId;
    private final String name;
    private final String description;
    private final List<Permission> permissions;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public RoleDTO(int roleId, String name, String description, List<Permission> permissions, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
