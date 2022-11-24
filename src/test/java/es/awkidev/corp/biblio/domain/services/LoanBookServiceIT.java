package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.TestConfig;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class LoanBookServiceIT {

    @Autowired
    private LoanBookService loanBookService;

    @Test
    void testCreate(){

        Book book = Book.builder().isbn("9788497443869")
                .build();

        List<CopyBook> copyBooks = List.of(
                CopyBook.builder().book(book).reference("ref-2")
                .build()
        );

        Customer customer = Customer.builder()
                .numberMembership("22222")
                .build();

        LoanBook loanBook = LoanBook.builder()
                .copyBooks(copyBooks).customer(customer)
                .build();

        StepVerifier
                .create(this.loanBookService.create(loanBook))
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertNotNull(result.getReference());
                    assertNotNull(result.getEndDate());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindByReferenceOk(){
        String loanReference = "loanRef-1";
        StepVerifier
                .create(this.loanBookService.findByReference(loanReference))
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertNotNull(result.getReference());
                    assertEquals(loanReference, result.getReference());
                    assertNotNull(result.getCustomer());
                    return true;
                })
                .thenCancel()
                .verify();
    }

}
