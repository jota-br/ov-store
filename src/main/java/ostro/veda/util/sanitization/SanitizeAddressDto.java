package ostro.veda.util.sanitization;

import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.UserDto;
import ostro.veda.util.enums.AddressType;

public class SanitizeAddressDto implements SanitizeFunction<AddressDto, AddressDto> {

    @Override
    public AddressDto sanitize(AddressDto addressDto) {
        int id = addressDto.getAddressId();
        UserDto userDTO = addressDto.getUser();
        String streetAddress = sanitizeString(addressDto.getStreetAddress());
        String addressNumber = sanitizeString(addressDto.getAddressNumber());
        AddressType addressType = addressDto.getAddressType();
        String city = sanitizeString(addressDto.getCity());
        String state = sanitizeString(addressDto.getState());
        String zipCode = sanitizeString(addressDto.getZipCode());
        String country = sanitizeString(addressDto.getCountry());
        boolean isActive = addressDto.isActive();

        return new AddressDto(id, userDTO, streetAddress, addressNumber, addressType, city, state, zipCode,
                country, isActive, addressDto.getCreatedAt(), addressDto.getUpdatedAt(), addressDto.getVersion());
    }
}
