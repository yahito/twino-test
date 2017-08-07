package twino.test.loan.services;

import org.junit.Test;
import twino.test.loan.locator.CountryResolver;
import twino.test.loan.locator.UserData;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class CountryResolverTest {
    @Test
    public void test_us_code_is_retrieved() throws Exception {
        CountryResolver resolver = new CountryResolver();
        UserData userData = new UserData();
        userData.setIp("8.8.8.8");
        Future<String> resolve = resolver.resolve(userData);
        assertEquals("US", resolve.get());
    }
}