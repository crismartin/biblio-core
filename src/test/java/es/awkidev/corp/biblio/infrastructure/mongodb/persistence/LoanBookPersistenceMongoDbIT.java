package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.TestConfig;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class LoanBookPersistenceMongoDbIT {

    @Autowired
    LoanBookPersistenceMongoDb loanBookPersistenceMongoDb;

    @Test
    void testCreate(){
        var book = Book.builder().isbn("9788497443869").build();
        var copyBook = CopyBook.builder().reference("ref-3").book(book).build();
        var customer = Customer.builder().numberMembership("33333").build();
        var loanBook = LoanBook.builder().reference("loanbook-t-1").copyBooks(List.of(copyBook)).customer(customer).build();

        StepVerifier
                .create(loanBookPersistenceMongoDb.create(loanBook))
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    //@Test -> Probar con MAX_LIMIT_LOANS = 1
    void testCreateKOForCustomerManyLoansThanLimit(){
        var book = Book.builder().isbn("9788457089895").build();
        var copyBook = CopyBook.builder().available(true).book(book).build();
        var customer = Customer.builder().numberMembership("11111").build();
        var loanBook = LoanBook.builder().copyBooks(List.of(copyBook)).customer(customer).build();

        StepVerifier
                .create(loanBookPersistenceMongoDb.create(loanBook))
                .expectError()
                .verify();
    }
}
