package twino.test.loan.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twino.test.loan.dto.ApplyResult;
import twino.test.loan.dto.LoanDto;
import twino.test.loan.dto.ResultCode;
import twino.test.loan.locator.LoanValidator;
import twino.test.loan.locator.LocationResolver;
import twino.test.loan.locator.UserData;
import twino.test.loan.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class LoanEndpoint {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LocationResolver locationResolver;

    @Autowired
    private CheckLoanService checkLoanService;

    @Autowired
    private CountryLimitService countryLimitService;

    @Autowired
    private LoanValidator loanValidator;

    @RequestMapping("/applyloan")
    public ApplyResult applyLoan(LoanDto loan, HttpServletRequest request) {
        ApplyResult result = new ApplyResult();
        result.setResultCode(ResultCode.SUCCESS.getCode());

        try {
            loanValidator.validate(loan);

            UserData userData = new UserData();
            userData.setIp(request.getRemoteAddr());

            String countryCode = locationResolver.resolve(userData);

            try {
                countryLimitService.start(countryCode);
            } catch (LimitException e) {
                result.setResultCode(ResultCode.TOO_MANY_REQUESTS.getCode());
                return result;
            }

            try {
                checkLoanService.checkAndAdd(loan);
            } catch (PersonIsBlackListedException e) {
                result.setResultCode(ResultCode.BLOCKED.getCode());
                return result;
            }
        } catch (Exception e) {
            result.setResultCode(ResultCode.OTHER.getCode());
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @RequestMapping("/allloans")
    public List<LoanDto> getAllLoans() {
        return loanService.getAll();
    }

    @RequestMapping("/userloans")
    public List<LoanDto> getUserLoans(long personId) {
        return loanService.getLoansForPerson(personId);
    }
}
