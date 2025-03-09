package ostro.veda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.enums.AddressType;
import ostro.veda.util.validation.annotation.*;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.ADDRESS)
public class AddressDto implements Dto {

    private final int addressId;

    @NotNull(message = "User cannot be null")
    @Setter
    private UserDto user;

    @NotBlank(message = "Street Address cannot be blank")
    @ValidStreetAddress
    private final String streetAddress;

    @NotBlank(message = "Address Number cannot be blank")
    @ValidAddressNumber
    private final String addressNumber;

    @NotNull(message = "Address Type cannot be null")
    private final AddressType addressType;

    @NotBlank(message = "City cannot be blank")
    @ValidCity
    private final String city;

    @NotBlank(message = "State cannot be blank")
    @ValidState
    private final String state;

    @NotBlank(message = "Zip Code cannot be blank")
    @ValidZipCode
    private final String zipCode;

    @NotBlank(message = "Country cannot be blank")
    @ValidCountry
    private final String country;

    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"addressId\":" + addressId)
                .add("\"streetAddress\":\"" + streetAddress + "\"")
                .add("\"addressNumber\":\"" + addressNumber + "\"")
                .add("\"addressType\":\"" + addressType.getType() + "\"")
                .add("\"city\":\"" + city + "\"")
                .add("\"state\":\"" + state + "\"")
                .add("\"zipCode\":\"" + zipCode + "\"")
                .add("\"country\":\"" + country + "\"")
                .add("\"isActive\":" + isActive)
                .add("\"createdAt\":\"" + createdAt + "\"")
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }
}
