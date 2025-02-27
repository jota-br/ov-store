package ostro.veda.db.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "user_id")
    private List<Address> addresses;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(String username, String salt, String hash, String email, String firstName,
                String lastName, String phone, boolean isActive, Role role) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isActive = isActive;
        this.role = role;
        this.addresses = new ArrayList<>();
    }

    public User(String username, String salt, String hash, String email, String firstName,
                String lastName, String phone, boolean isActive, Role role, List<Address> addresses) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isActive = isActive;
        this.role = role;
        this.addresses = addresses == null ? new ArrayList<>() : addresses;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getHash() {
        return hash;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public Role getRole() {
        return role;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    public UserDTO transformToDto() {
        List<AddressDTO> addressDTOList = new ArrayList<>();
        if (this.getAddresses().isEmpty()) {
            for (Address address : this.getAddresses()) {
                addressDTOList.add(new AddressDTO(address.getAddressId(), address.getUserId(), address.getStreetAddress(),
                        address.getAddressNumber(), address.getAddressType(), address.getCity(), address.getState(),
                        address.getZipCode(), address.getCountry(), address.isActive(), address.getCreatedAt(), address.getUpdatedAt()));
            }
        }
        return new UserDTO(this.getUserId(), this.getUsername(), this.getSalt(), this.getHash(), this.getEmail(), this.getFirstName(), this.getLastName(),
                this.getPhone(), this.isActive(), this.getRole(), addressDTOList, this.getCreatedAt(), this.getUpdatedAt());
    }
}
