package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.domain.model.validations.ListNotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanNewDto {

    @NotEmpty
    private String numberMembership;

    @ListNotEmpty
    private List<String> books;

    public LoanBook toLoanBook(){
        LoanBook loanBook = new LoanBook();
        Customer customer = new Customer();

        customer.setNumberMembership(numberMembership);
        loanBook.setCustomer(customer);

        List<CopyBook> copyBooks = books.stream()
                .map(book -> CopyBook.builder()
                        .reference(book)
                        .build())
                .collect(Collectors.toList());

        loanBook.setCopyBooks(copyBooks);
        return loanBook;
    }
}
