package ostro.veda.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableName;
import ostro.veda.repository.dao.Permission;
import ostro.veda.util.validation.annotation.ValidDescription;
import ostro.veda.util.validation.annotation.ValidName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.ROLE)
public class RoleDto implements Dto {

    private final int roleId;

    @NotBlank(message = "Name cannot be blank")
    @ValidName
    private final String name;

    @ValidDescription
    private final String description;

    @Valid
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
