package twino.test.loan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twino.test.loan.dto.LoanDto;

import javax.transaction.Transactional;

@Component
@Transactional
public class CheckLoanService {
    private final BlacklistService blacklistService;
    private final LoanService loanService;

    @Autowired
    public CheckLoanService(BlacklistService blacklistService, LoanService loanService) {
        this.blacklistService = blacklistService;
        this.loanService = loanService;
    }

    public void checkAndAdd(LoanDto loanDto) throws PersonIsBlackListedException {
        if(blacklistService.isBlacklisted(loanDto.getPersonId())) {
            throw new PersonIsBlackListedException();
        }

        loanService.insertLoan(loanDto);
    }
}
