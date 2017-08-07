package twino.test.loan.rest;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twino.test.loan.dto.ApplyResult;
import twino.test.loan.dto.LoanDto;
import twino.test.loan.dto.ResultCode;
import twino.test.loan.locator.LoanValidator;
import twino.test.loan.locator.LocationResolver;
import twino.test.loan.locator.UserData;
import twino.test.loan.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanEndpointTest {

    @Mock
    private LoanService loanService;

    @Mock
    private LocationResolver locationResolver;

    @Mock
    private CheckLoanService checkLoanService;

    @Mock
    private CountryLimitService countryLimitService;

    @Mock
    private LoanValidator loanValidator;

    @InjectMocks
    private LoanEndpoint loanEndpoint;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_validation_fail() {
        LoanDto loan = new LoanDto();
        HttpServletRequest mock = mock(HttpServletRequest.class);
        doThrow(new ValidationException("validation error")).when(loanValidator).validate(any(LoanDto.class));
        ApplyResult result = loanEndpoint.applyLoan(loan, mock);
        assertEquals(ResultCode.OTHER.getCode(), result.getResultCode());
        assertEquals("validation error", result.getDescription());
    }

    @Test
    public void test_resolve_location_fail() {
        LoanDto loan = new LoanDto();
        HttpServletRequest mock = mock(HttpServletRequest.class);
        doThrow(new RuntimeException("some ex")).when(locationResolver).resolve(any(UserData.class));
        ApplyResult result = loanEndpoint.applyLoan(loan, mock);
        assertEquals(ResultCode.OTHER.getCode(), result.getResultCode());
        assertEquals("some ex", result.getDescription());
    }

    @Test
    public void test_limit_fail() throws LimitException {
        LoanDto loan = new LoanDto();
        HttpServletRequest mock = mock(HttpServletRequest.class);
        doThrow(new LimitException()).when(countryLimitService).start(anyString());

        ApplyResult result = loanEndpoint.applyLoan(loan, mock);
        assertEquals(ResultCode.TOO_MANY_REQUESTS.getCode(), result.getResultCode());
        assertEquals("", result.getDescription());
    }

    @Test
    public void test_block_fail() throws PersonIsBlackListedException {
        LoanDto loan = new LoanDto();
        HttpServletRequest mock = mock(HttpServletRequest.class);
        doThrow(new PersonIsBlackListedException()).when(checkLoanService).checkAndAdd(any(LoanDto.class));
        ApplyResult result = loanEndpoint.applyLoan(loan, mock);
        assertEquals(ResultCode.BLOCKED.getCode(), result.getResultCode());
        assertEquals("", result.getDescription());
    }

    @Test
    public void test_success() throws PersonIsBlackListedException {
        LoanDto loan = new LoanDto();
        HttpServletRequest mock = mock(HttpServletRequest.class);
        ApplyResult result = loanEndpoint.applyLoan(loan, mock);
        assertEquals(ResultCode.SUCCESS.getCode(), result.getResultCode());
        assertEquals("", result.getDescription());
    }

    @Test
    public void test_get_user_loans() {
        ArrayList<LoanDto> loans = Lists.newArrayList();
        loans.add(new LoanDto());
        when(loanService.getLoansForPerson(eq(1L))).thenReturn(loans);

        List<LoanDto> userLoans = loanEndpoint.getUserLoans(1L);
        assertEquals(loans.size(), userLoans.size());
        assertEquals(loans.get(0), userLoans.get(0));
    }

    @Test
    public void test_all_loans() {
        ArrayList<LoanDto> loans = Lists.newArrayList();
        loans.add(new LoanDto());
        when(loanService.getAll()).thenReturn(loans);

        List<LoanDto> userLoans = loanEndpoint.getAllLoans();
        assertEquals(loans.size(), userLoans.size());
        assertEquals(loans.get(0), userLoans.get(0));
    }
}