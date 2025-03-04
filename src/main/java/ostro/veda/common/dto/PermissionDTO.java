package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class PermissionDTO {

    private final int permissionId;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"permissionId\":" + permissionId)
                .add("\"name\":\"" + name + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"createdAt\":" + createdAt)
                .add("\"updatedAt\":" + updatedAt)
                .toString();
    }
}
