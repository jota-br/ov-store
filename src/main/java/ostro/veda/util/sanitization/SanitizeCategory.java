package ostro.veda.util.sanitization;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.model.dto.CategoryDto;

@Slf4j
public class SanitizeCategory implements SanitizeFunction<CategoryDto, CategoryDto> {

    @Override
    public CategoryDto sanitize(CategoryDto categoryDto) {

        log.info("Sanitizing = {}", categoryDto.getClass().getSimpleName());

        int categoryId = categoryDto.getCategoryId();
        String categoryName = categoryDto.getName();
        String categoryDescription = categoryDto.getDescription();

        categoryName = sanitizeString(categoryName);
        categoryDescription = sanitizeString(categoryDescription);

        return new CategoryDto(categoryId, categoryName, categoryDescription, categoryDto.isActive(),
                categoryDto.getCreatedAt(), categoryDto.getUpdatedAt(), categoryDto.getVersion());
    }
}
