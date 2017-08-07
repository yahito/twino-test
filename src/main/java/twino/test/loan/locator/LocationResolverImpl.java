package twino.test.loan.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class LocationResolverImpl implements LocationResolver {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final long locationResolveRequestTimeoutMillis;
    private final String locationDefaultValue;

    private final AsyncLocationResolver asyncLocationResolver;

    public LocationResolverImpl(@Value("${location.resolve.request.timeout.millis}") long locationResolveRequestTimeoutMillis,
                                @Value("${location.default.value}") String locationDefaultValue,
                                @Autowired AsyncLocationResolver asyncLocationResolver) {
        this.locationResolveRequestTimeoutMillis = locationResolveRequestTimeoutMillis;
        this.locationDefaultValue = checkNotNull(locationDefaultValue, "locationDefaultValue is null");
        this.asyncLocationResolver = asyncLocationResolver;
    }

    @Override
    @NotNull
    public String resolve(UserData userData) {
        try {
            return asyncLocationResolver.resolve(userData).get(locationResolveRequestTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return locationDefaultValue;
    }
}
