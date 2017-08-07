package twino.test.loan.mapping;

import org.mapstruct.Mapper;
import twino.test.loan.dao.Loan;
import twino.test.loan.dto.LoanDto;

import java.util.List;

@Mapper
public interface LoanMapper {

    Loan loanDtoToLoan(LoanDto loanDto);
    List<Loan> loanDtoToLoan(List<LoanDto> loanDto);
    LoanDto loanToLoanDto(Loan loan);
    List<LoanDto> loanToLoanDto(List<Loan> loan);
}
