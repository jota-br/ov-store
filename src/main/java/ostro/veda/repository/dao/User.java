package ostro.veda.repository.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    // acceptable characters @ _ - [a-zA-Z0-9]
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone", length = 15) // E.164 format // +5547123456789 // +DDI DDD Number
    private String phone;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id", referencedColumnName = "role_id")
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Address> addresses;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    public User() {
    }

    public User updateUser(User updatedData) {
        this.username = updatedData.getUsername();
        this.salt = updatedData.getSalt();
        this.hash = updatedData.getHash();
        this.email = updatedData.getEmail();
        this.firstName = updatedData.getFirstName();
        this.lastName = updatedData.getLastName();
        this.phone = updatedData.getPhone();
        this.isActive = updatedData.isActive();
        this.role = updatedData.getRole();

        return this;
    }

    public UserDto transformToDto() {
        List<AddressDto> addressDtoList = new ArrayList<>();
        for (Address address : this.getAddresses()) {
            addressDtoList.add(
                    new AddressDto(address.getAddressId(), null, address.getStreetAddress(),
                            address.getAddressNumber(), address.getAddressType(), address.getCity(), address.getState(),
                            address.getZipCode(), address.getCountry(), address.isActive(), address.getCreatedAt(),
                            address.getUpdatedAt(), this.getVersion())
            );
        }
        return new UserDto(this.getUserId(), this.getUsername(), this.getSalt(), this.getHash(),
                this.getEmail(), this.getFirstName(), this.getLastName(), this.getPhone(),
                this.isActive(), this.getRole().transformToDto(), addressDtoList, this.getCreatedAt(),
                this.getUpdatedAt(), this.getVersion());
    }
}
