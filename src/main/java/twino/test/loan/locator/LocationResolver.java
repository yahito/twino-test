package twino.test.loan.locator;

import javax.validation.constraints.NotNull;

public interface LocationResolver {
    @NotNull
    String resolve(UserData userData);
}
