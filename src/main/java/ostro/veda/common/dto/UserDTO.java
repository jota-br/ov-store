package ostro.veda.common.dto;

import ostro.veda.db.jpa.Role;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {

    private final int userId;
    private final String username;
    private final String salt;
    private final String hash;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final boolean isActive;
    private final Role role;
    private List<AddressDTO> addresses;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserDTO(int userId, String username, String salt, String hash, String email, String firstName,
                   String lastName, String phone, boolean isActive, Role role, List<AddressDTO> addresses,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.salt = salt;
        this.hash = hash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isActive = isActive;
        this.role = role;
        this.addresses = addresses;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
