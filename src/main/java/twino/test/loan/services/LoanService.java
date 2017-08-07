package twino.test.loan.services;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twino.test.loan.dao.LoanDao;
import twino.test.loan.dto.LoanDto;
import twino.test.loan.mapping.LoanMapper;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class LoanService {
    private LoanDao loanDao;
    private LoanMapper loanMapper = Mappers.getMapper(LoanMapper.class);

    @Autowired
    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }


    public List<LoanDto> getAll() {
        return loanMapper.loanToLoanDto(loanDao.getAll());
    }

    public List<LoanDto> getLoansForPerson(long personId) {
        return loanMapper.loanToLoanDto(loanDao.getLoansForPerson(personId));
    }

    public void insertLoan(LoanDto loanDto) {
        loanDao.insertLoan(loanMapper.loanDtoToLoan(loanDto));
    }

}
