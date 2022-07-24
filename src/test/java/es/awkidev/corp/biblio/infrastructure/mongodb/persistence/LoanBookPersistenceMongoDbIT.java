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
        List<Book> books = List.of(
                Book.builder().isbn("9788457089895").build()
        );

        Customer customer = Customer.builder()
                .numberMembership("11111")
                .build();

        LoanBook loanBook = LoanBook.builder()
                .books(books)
                .customer(customer)
                .build();

        StepVerifier
                .create(this.loanBookPersistenceMongoDb.create(loanBook))
                .expectNextMatches(result -> {
                    assertTrue(result);
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
