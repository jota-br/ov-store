package ostro.veda.dto;

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

    public UserDTO(int userId, String username, String salt, String hash, String email,
                   String firstName, String lastName, String phone, boolean isActive) {
        this.userId = userId;
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
}
