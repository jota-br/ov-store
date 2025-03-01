package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AddressDTO {

    private final int addressId;
    private final int userId;
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
