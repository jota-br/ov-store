package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class AddressDTO {

    private final int addressId;

    @Setter
    private UserDTO user;

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
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"addressId\":" + addressId)
                .add("\"streetAddress\":\"" + streetAddress + "\"")
                .add("\"addressNumber\":\"" + addressNumber + "\"")
                .add("\"addressType\":\"" + addressType + "\"")
                .add("\"city\":\"" + city + "\"")
                .add("\"state\":\"" + state + "\"")
                .add("\"zipCode\":\"" + zipCode + "\"")
                .add("\"country\":\"" + country + "\"")
                .add("\"isActive\":" + isActive)
                .add("\"createdAt\":" + createdAt)
                .add("\"updatedAt\":" + updatedAt)
                .toString();
    }
}
