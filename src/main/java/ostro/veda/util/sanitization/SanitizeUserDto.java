package ostro.veda.util.sanitization;

import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class SanitizeUserDto implements SanitizeFunction<UserDto, UserDto> {

    @Override
    public UserDto apply(UserDto userDto) {
        String firstName = sanitize(userDto.getFirstName());
        String lastName = sanitize(userDto.getLastName());
        String username = sanitize(userDto.getUsername());
        String email = sanitize(userDto.getEmail());

        List<AddressDto> addressDtoList = new ArrayList<>();

        SanitizeAddressDto sanitizeAddressDto = new SanitizeAddressDto();
        for (AddressDto addressDto : userDto.getAddresses()) {
            addressDtoList.add(sanitizeAddressDto.apply(addressDto));
        }

        return new UserDto(userDto.getUserId(), username, userDto.getSalt(), userDto.getHash(), email, firstName,
                lastName, userDto.getPhone(), userDto.isActive(), userDto.getRole(), addressDtoList,
                userDto.getCreatedAt(), userDto.getUpdatedAt(), userDto.getVersion());
    }
}
