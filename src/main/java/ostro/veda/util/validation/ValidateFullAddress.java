package ostro.veda.util.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.FullAddressGeoapify;
import ostro.veda.util.sanitization.EncodeUrl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class ValidateFullAddress {

    final String API_KEY = "a6f3fccde6f646db92ca0c8ad3c9fb1e";

    public boolean isValid(AddressDto addressDto) {

        EncodeUrl encodeUrl = new EncodeUrl();
//        String FULL_ADDRESS = encodeUrl.encodeUrl(addressDto.getFullAddress());
        String FULL_ADDRESS = encodeUrl.encodeUrl("Rua Anita Garibaldi, 1604, Joinville, Santa Catarina, Brazil, 89203301");
        log.info("Validating address = {}", addressDto.getFullAddress());
        try {
            HttpClient client = HttpClient.newHttpClient();
            String URL = "https://api.geoapify.com/v1/geocode/search?text=%S&apiKey=%s"
                    .formatted(FULL_ADDRESS, API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode properties = root.at("/features/0/properties");

                FullAddressGeoapify fullAddressGeoapify = mapper.treeToValue(properties, FullAddressGeoapify.class);

                return fullAddressGeoapify.getStreet().equalsIgnoreCase(addressDto.getStreetAddress()) &&
                        fullAddressGeoapify.getCity().equalsIgnoreCase(addressDto.getCity()) &&
                        fullAddressGeoapify.getState().equalsIgnoreCase(addressDto.getState()) &&
                        fullAddressGeoapify.getCountry().equalsIgnoreCase(addressDto.getCountry());
            } else
                throw new RuntimeException("API request failed with response code %d".formatted(response.statusCode()));

        } catch (IOException | InterruptedException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
