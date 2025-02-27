package ostro.veda.common.dto;

import java.time.LocalDateTime;

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

    public int getAddressId() {
        return addressId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
