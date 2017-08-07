package twino.test.loan.locator;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Future;

public interface AsyncLocationResolver {
    @NotNull
    Future<String> resolve(UserData userData);
}
