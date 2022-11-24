package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.LoanBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanInfoDto {
    private String reference;
    private String customerName;
    private LocalDate endDate;

    public LoanInfoDto(LoanBook loanBook){
        setReference(loanBook.getReference());
        setCustomerName(loanBook.getCustomer().getName());
        setEndDate(loanBook.getEndDate());
    }
}
