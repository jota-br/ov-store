package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CategoryDTO {

    private final int categoryId;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;
}
