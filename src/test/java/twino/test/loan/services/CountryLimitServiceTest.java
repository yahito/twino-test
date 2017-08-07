package twino.test.loan.services;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CountryLimitServiceTest {

    private CountryLimitService countryLimitService;

    @Test
    public void test_rate_per_country_is_not_exceeded() throws Exception {
        Random r = new Random(System.nanoTime());

        countryLimitService = new CountryLimitService(5);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ConcurrentHashMap<Long, AtomicInteger> successRequests = new ConcurrentHashMap<>();
        long startTest = System.nanoTime();

        for(int i = 0; i < 100; i++) {
            Thread.sleep(r.nextInt(100));

            executorService.submit(() -> {
                long sec = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTest);
                successRequests.computeIfAbsent(sec, s->new AtomicInteger());
                try {
                    countryLimitService.start("uk");
                    successRequests.computeIfPresent(sec, (k,v)-> {
                        v.incrementAndGet();
                        return v;
                    });
                } catch (LimitException ignore) {}

            });
        }

        executorService.awaitTermination(10, TimeUnit.SECONDS);

        Set<Map.Entry<Long, AtomicInteger>> entries = successRequests.entrySet();
        Assert.assertTrue(entries.size() > 0);
        for (Map.Entry<Long, AtomicInteger> entry : entries) {
            Assert.assertTrue(entry.getValue().get() <= 5);
        }
    }
}