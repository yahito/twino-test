package twino.test.loan.services;

import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class CountryLimitService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final int maxRequestPerSecond;

    private ConcurrentHashMap<String, TimedSemaphore> semaphoreMap = new ConcurrentHashMap<>();

    public CountryLimitService(@Value("${max.request.per.second}") int maxRequestPerSecond) {
        this.maxRequestPerSecond = maxRequestPerSecond;
    }

    public void start(String countyCode) throws LimitException {
        TimedSemaphore semaphore = semaphoreMap.computeIfAbsent(countyCode,
                s -> new TimedSemaphore(1L, TimeUnit.SECONDS, maxRequestPerSecond));

        if(!semaphore.tryAcquire()) {
            logger.debug("limit is archived");
            throw new LimitException();
        }

        logger.debug("ok");
    }
}
