package ostro.veda.util.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.FullAddressGeoapify;
import ostro.veda.util.helpers.ApiGetAddress;

@Slf4j
public class ValidateFullAddress {

    public boolean isValid(AddressDto addressDto) {

        log.info("Validating address = {}", addressDto.getFullAddress());

        try {
            ApiGetAddress apiGetAddress = new ApiGetAddress();
            String response = apiGetAddress.fetch(addressDto.getFullAddress());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode properties = root.at("/features/0/properties");

            FullAddressGeoapify fullAddressGeoapify = mapper.treeToValue(properties, FullAddressGeoapify.class);

            return fullAddressGeoapify.getStreet().equalsIgnoreCase(addressDto.getStreetAddress()) &&
                    fullAddressGeoapify.getCity().equalsIgnoreCase(addressDto.getCity()) &&
                    fullAddressGeoapify.getState().equalsIgnoreCase(addressDto.getState()) &&
                    fullAddressGeoapify.getCountry().equalsIgnoreCase(addressDto.getCountry());

        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
