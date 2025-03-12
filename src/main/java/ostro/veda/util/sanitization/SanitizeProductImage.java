package ostro.veda.util.sanitization;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.model.dto.ProductImageDto;

@Slf4j
public class SanitizeProductImage implements SanitizeFunction<ProductImageDto, ProductImageDto> {

    @Override
    public ProductImageDto sanitize(ProductImageDto productImageDto) {

        log.info("Sanitizing = {}", productImageDto.getClass().getSimpleName());

        int productImageId = productImageDto.getProductImageId();
        String url = productImageDto.getImageUrl();
        EncodeUrl encodeUrl = new EncodeUrl();
        url = encodeUrl.encodeUrl(url);

        return new ProductImageDto(productImageId, url, productImageDto.isMain(), productImageDto.getVersion());
    }
}
