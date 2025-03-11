package ostro.veda.util.sanitization;

import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.UserDto;
import ostro.veda.util.enums.AddressType;

public class SanitizeAddressDto implements SanitizeFunction<AddressDto, AddressDto> {

    @Override
    public AddressDto apply(AddressDto AddressDto) {
        int id = AddressDto.getAddressId();
        UserDto userDTO = AddressDto.getUser();
        String streetAddress = sanitize(AddressDto.getStreetAddress());
        String addressNumber = sanitize(AddressDto.getAddressNumber());
        AddressType addressType = AddressDto.getAddressType();
        String city = sanitize(AddressDto.getCity());
        String state = sanitize(AddressDto.getState());
        String zipCode = sanitize(AddressDto.getZipCode());
        String country = sanitize(AddressDto.getCountry());
        boolean isActive = AddressDto.isActive();

        return new AddressDto(id, userDTO, streetAddress, addressNumber, addressType, city, state, zipCode,
                country, isActive, AddressDto.getCreatedAt(), AddressDto.getUpdatedAt(), AddressDto.getVersion());
    }
}
