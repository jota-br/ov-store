package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableNames;
import ostro.veda.repository.dao.Permission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.ROLE)
public class RoleDto implements Dto {

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
