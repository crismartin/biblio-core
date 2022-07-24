package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

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
class LoanBookPersistenceMongoDbIT {

    @Autowired
    LoanBookPersistenceMongoDb loanBookPersistenceMongoDb;

    @Test
    void testCreate(){
        var book = Book.builder().isbn("9788457089895").build();
        var customer = Customer.builder().numberMembership("11111").build();
        var loanBook = LoanBook.builder().books(List.of(book)).customer(customer).build();

        StepVerifier
                .create(this.loanBookPersistenceMongoDb.create(loanBook))
                .expectNextMatches(result -> {
                    assertTrue(result);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    //@Test -> Probar con MAX_LIMIT_LOANS = 1
    void testCreateKOForCustomerManyLoansThanLimit(){
        var book = Book.builder().isbn("9788457089895").build();
        var customer = Customer.builder().numberMembership("33333").build();
        var loanBook = LoanBook.builder().books(List.of(book)).customer(customer).build();

        StepVerifier
                .create(this.loanBookPersistenceMongoDb.create(loanBook))
                .expectError()
                .verify();
    }
}
