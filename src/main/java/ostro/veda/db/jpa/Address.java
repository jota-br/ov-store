package ostro.veda.db.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.AddressDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "address_number", nullable = false, length = 50)
    private String addressNumber;

    @Column(name = "address_type", nullable = false, length = 50)
    private String addressType;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 50)
    private String zipCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Address() {
    }

    public Address(int userId, String streetAddress, String addressNumber, String addressType,
                   String city, String state, String zipCode, String country, boolean isActive) {
        this.userId = userId;
        this.streetAddress = streetAddress;
        this.addressNumber = addressNumber;
        this.addressType = addressType;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.isActive = isActive;
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

    public Address updateAddress(Address updatedData) {
        this.streetAddress = updatedData.getStreetAddress();
        this.addressNumber = updatedData.getAddressNumber();
        this.addressType = updatedData.getAddressType();
        this.city = updatedData.getCity();
        this.state = updatedData.getState();
        this.zipCode = updatedData.getZipCode();
        this.country = updatedData.getCountry();
        this.isActive = updatedData.isActive();
        this.userId = updatedData.getUserId();

        return this;
    }

    public AddressDTO transformToDto() {
        return new AddressDTO(this.getAddressId(), this.getUserId(), this.getStreetAddress(), this.getAddressNumber(), this.getAddressType(),
                this.getCity(), this.getState(), this.getZipCode(), this.getCountry(), this.isActive(), this.getCreatedAt(), this.getUpdatedAt());
    }
}
