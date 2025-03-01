package ostro.veda.db.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.AddressDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
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

    @Version
    private int version;

    public Address() {
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

        return this;
    }

    public AddressDTO transformToDto() {
        return new AddressDTO(this.getAddressId(), this.getUserId(), this.getStreetAddress(),
                this.getAddressNumber(), this.getAddressType(), this.getCity(), this.getState(),
                this.getZipCode(), this.getCountry(), this.isActive(), this.getCreatedAt(),
                this.getUpdatedAt(), this.getVersion());
    }
}
