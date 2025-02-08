package ostro.veda.jpa;

import jakarta.persistence.*;
import ostro.veda.dto.UserDTO;

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

    @Column(name = "is_active", columnDefinition = "Boolean Default false")
    private boolean isActive;

    public User() {
    }

    public User(String username, String salt, String hash, String email, String firstName, String lastName, String phone, boolean isActive) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isActive = isActive;
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

    public UserDTO transformToDto() {
        return new UserDTO(this.getUserId(), this.getUsername(), this.getSalt(), this.getHash(), this.getEmail(), this.getFirstName(), this.getLastName(),
                this.getPhone(), this.isActive());
    }
}
