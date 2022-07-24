package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.TestConfig;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class LoanBookServiceIT {

    @Autowired
    private LoanBookService loanBookService;

    @Test
    void testCreate(){

        List<Book> books = List.of(
                Book.builder().isbn("9788457089870").build()
        );

        Customer customer = Customer.builder()
                .numberMembership("11111")
                .build();

        LoanBook loanBook = LoanBook.builder()
                .books(books)
                .customer(customer)
                .build();

        StepVerifier
                .create(this.loanBookService.create(loanBook))
                .expectNextMatches(result -> {
                    assertTrue(result);
                    return true;
                })
                .thenCancel()
                .verify();
    }

}
