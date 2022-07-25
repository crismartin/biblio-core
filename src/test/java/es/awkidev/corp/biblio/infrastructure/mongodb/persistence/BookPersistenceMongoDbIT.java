package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class BookPersistenceMongoDbIT {

    @Autowired
    private BookPersistenceMongoDb bookPersistenceMongoDb;

    @Test
    void testFindByIsbnOk(){
        String isbn = "9788425223280";

        StepVerifier
                .create(this.bookPersistenceMongoDb.searchByIsbn(isbn))
                .expectNextMatches(book -> {
                    assertNotNull(book);
                    assertEquals(isbn, book.getIsbn());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
