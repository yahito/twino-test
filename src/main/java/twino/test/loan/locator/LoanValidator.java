package twino.test.loan.locator;

import org.mapstruct.ap.internal.util.Strings;
import org.springframework.stereotype.Component;
import twino.test.loan.dto.LoanDto;

import javax.validation.ValidationException;

@Component
public class LoanValidator {
    public void validate(LoanDto loanDto) {
        if(loanDto.getLoanAmount() <= 0) {
            throw new ValidationException("amount is negative or zero");
        }
        if(Strings.isEmpty(loanDto.getName())) {
            throw new ValidationException("name is empty");
        }

        if(Strings.isEmpty(loanDto.getSurname())) {
            throw new ValidationException("surname is empty");
        }

        if(loanDto.getPersonId() < 0) {
            throw new ValidationException("personId is negative");
        }

        if(loanDto.getTerm() <= 0) {
            throw new ValidationException("term is negative or zero");
        }
    }
}
