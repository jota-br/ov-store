package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AddressDTO {

    private final int addressId;

    @Setter
    private UserDTO user;

    private final String streetAddress;
    private final String addressNumber;
    private final String addressType;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;
}
