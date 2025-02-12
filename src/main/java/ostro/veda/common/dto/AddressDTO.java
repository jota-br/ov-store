package ostro.veda.common.dto;

import java.time.LocalDateTime;

public class AddressDTO {

    private final int addressId;
    private final String streetAddress;
    private final String addressNumber;
    private final String addressType;
    private final String city;
    private final String state;
    private final String zip_code;
    private final String country;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AddressDTO(int addressId, String streetAddress, String addressNumber, String addressType,
                      String city, String state, String zip_code, String country, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.addressId = addressId;
        this.streetAddress = streetAddress;
        this.addressNumber = addressNumber;
        this.addressType = addressType;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.country = country;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getAddressId() {
        return addressId;
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

    public String getZip_code() {
        return zip_code;
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
