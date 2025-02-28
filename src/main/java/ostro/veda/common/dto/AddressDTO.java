package ostro.veda.common.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
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

    public AddressDTO(int addressId, int userId, String streetAddress, String addressNumber, String addressType,
                      String city, String state, String zipCode, String country, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.addressId = addressId;
        this.userId = userId;
        this.streetAddress = streetAddress;
        this.addressNumber = addressNumber;
        this.addressType = addressType;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AddressDTO(int userId, String streetAddress, String addressNumber, String addressType,
                      String city, String state, String zipCode, String country, boolean isActive) {
        this(0, userId, streetAddress, addressNumber, addressType, city, state, zipCode,
                country, isActive, null, null);
    }
}
