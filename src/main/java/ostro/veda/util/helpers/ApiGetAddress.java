package ostro.veda.util.helpers;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.util.sanitization.EncodeUrl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class ApiGetAddress {

    // myprojects.geoapify.com
    // to get a valid api key
    final String API_KEY = "a6f3fccde6f646db92ca0c8ad3c9fb1e";

    public String fetch(String string) {
        EncodeUrl encodeUrl = new EncodeUrl();
//        String FULL_ADDRESS = encodeUrl.encodeUrl(addressDto.getFullAddress());
        String FULL_ADDRESS = encodeUrl.encodeUrl(string);

        log.info("ApiGetAddress = {}", string);

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
                return response.body();
            } else {
                throw new RuntimeException("API request failed with response code %d".formatted(response.statusCode()));
            }

        } catch (IOException | InterruptedException | RuntimeException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
