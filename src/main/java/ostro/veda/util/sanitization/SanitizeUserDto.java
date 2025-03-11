package ostro.veda.util.sanitization;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SanitizeUserDto implements SanitizeFunction<UserDto, UserDto> {

    @Override
    public UserDto sanitize(UserDto userDto) {

        log.info("Sanitizing = {}", userDto.getClass().getSimpleName());

        String firstName = sanitizeString(userDto.getFirstName());
        String lastName = sanitizeString(userDto.getLastName());
        String username = sanitizeString(userDto.getUsername());
        String email = sanitizeString(userDto.getEmail());

        List<AddressDto> addressDtoList = new ArrayList<>();

        SanitizeAddressDto sanitizeAddressDto = new SanitizeAddressDto();
        for (AddressDto addressDto : userDto.getAddresses()) {
            addressDtoList.add(sanitizeAddressDto.sanitize(addressDto));
        }

        return new UserDto(userDto.getUserId(), username, userDto.getSalt(), userDto.getHash(), email, firstName,
                lastName, userDto.getPhone(), userDto.isActive(), userDto.getRole(), addressDtoList,
                userDto.getCreatedAt(), userDto.getUpdatedAt(), userDto.getVersion());
    }
}
