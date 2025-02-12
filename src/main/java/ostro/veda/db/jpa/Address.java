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
    private String zip_code;

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

    public Address(String streetAddress, String addressNumber, String addressType, String city, String state, String zip_code, String country, boolean isActive) {
        this.streetAddress = streetAddress;
        this.addressNumber = addressNumber;
        this.addressType = addressType;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.country = country;
        this.isActive = isActive;
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

    public AddressDTO transformToDto() {
        return new AddressDTO(this.getAddressId(), this.getStreetAddress(), this.getAddressNumber(), this.getAddressType(),
                this.getCity(), this.getState(), this.getZip_code(), this.getCountry(), this.isActive(), this.getCreatedAt(), this.getUpdatedAt());
    }
}
