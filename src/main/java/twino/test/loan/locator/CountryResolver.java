package twino.test.loan.locator;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class CountryResolver implements AsyncLocationResolver {
    private final RestTemplate restTemplate = new RestTemplate();
    private final static String SERVICE_URL = "http://freegeoip.net/json/";

    @Async
    @Override
    @NotNull
    public Future<String> resolve(UserData userData) {
        ResponseEntity<Locator> response = restTemplate.getForEntity(SERVICE_URL + userData.getIp(), Locator.class);
        return CompletableFuture.completedFuture(response.getBody().getCountry_code());
    }
}
