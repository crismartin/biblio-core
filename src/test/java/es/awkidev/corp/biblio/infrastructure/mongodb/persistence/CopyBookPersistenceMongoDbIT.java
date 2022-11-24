package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.TestConfig;
import es.awkidev.corp.biblio.domain.persistence.CopyBookPersistence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class CopyBookPersistenceMongoDbIT {

    @Autowired
    private CopyBookPersistence copyBookPersistence;

    @Test
    void testSearchByBookIsbnAvailableOk(){
        String isbn = "9788497443869";

        StepVerifier
                .create(copyBookPersistence.searchByBookIsbnAvailable(isbn))
                .expectNextMatches(copyBook -> {
                    assertNotNull(copyBook);
                    assertNotNull(copyBook.getBook());
                    assertEquals(isbn, copyBook.getBookIsbn());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGetByReferenceOk(){
        String reference = "ref-5";

        StepVerifier
                .create(copyBookPersistence.getByReference(reference))
                .expectNextMatches(copyBook -> {
                    assertNotNull(copyBook);
                    assertNotNull(copyBook.getBook());
                    assertEquals(reference, copyBook.getReference());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
