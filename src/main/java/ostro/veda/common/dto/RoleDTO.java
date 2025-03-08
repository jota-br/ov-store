package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.common.util.Auditable;
import ostro.veda.common.util.TableNames;
import ostro.veda.db.jpa.Permission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.ROLE)
public class RoleDTO {

    private final int roleId;
    private final String name;
    private final String description;
    private final List<Permission> permissions;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"roleId\":" + roleId)
                .add("\"name\":\"" + name + "\"")
                .add("\"description\":\"" + description + "\"")
//                .add("\"permissions\":" + permissions)
                .add("\"createdAt\":\"" + createdAt + "\"")
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }

    private final int version;
}
