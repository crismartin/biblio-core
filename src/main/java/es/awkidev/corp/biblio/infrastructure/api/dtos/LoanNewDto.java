package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.domain.model.validations.ListNotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
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

        loanBook.setBooks(
                !CollectionUtils.isEmpty(books)
                ? books.stream()
                    .map(isbn -> Book.builder().isbn(isbn).build())
                    .collect(Collectors.toList())
                : Collections.emptyList()
        );

        return loanBook;
    }
}
