package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.db.jpa.Permission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class RoleDTO {

    private final int roleId;
    private final String name;
    private final String description;
    private final List<Permission> permissions;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"roleId\":" + roleId)
                .add("\"name\":\"" + name + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"permissions\":" + permissions)
                .add("\"createdAt\":" + createdAt)
                .add("\"updatedAt\":" + updatedAt)
                .toString();
    }

    private final int version;
}
