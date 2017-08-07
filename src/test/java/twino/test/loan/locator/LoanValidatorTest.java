package twino.test.loan.locator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import twino.test.loan.dto.LoanDto;

import javax.validation.ValidationException;

public class LoanValidatorTest {
    private final LoanDto loanDto = new LoanDto();

    private final LoanValidator loanValidator = new LoanValidator();

    @Before
    public void setUp() throws Exception {
        loanDto.setTerm(1L);
        loanDto.setSurname("surname");
        loanDto.setPersonId(1L);
        loanDto.setName("name");
        loanDto.setLoanAmount(1000);
    }

    @Test
    public void test_success() {
        loanValidator.validate(loanDto);
    }

    @Test(expected = ValidationException.class)
    public void test_amount_is_negative() {
        loanDto.setLoanAmount(-1);
        loanValidator.validate(loanDto);
    }

    @Test(expected = ValidationException.class)
    public void test_name_is_null() {
        loanDto.setName(null);
        loanValidator.validate(loanDto);
    }

    @Test(expected = ValidationException.class)
    public void test_surname_is_null() {
        loanDto.setSurname(null);
        loanValidator.validate(loanDto);
    }

    @Test(expected = ValidationException.class)
    public void test_term_is_negative() {
        loanDto.setTerm(-1);
        loanValidator.validate(loanDto);
    }
}