package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class BookReactiveIT {

    @Autowired
    private BookReactive bookReactive;

    @Test
    void testFindBookEntityByIsbn(){
        String isbn = "9788425223280";

        StepVerifier
                .create(this.bookReactive.findBookEntityByIsbn(isbn))
                .expectNextMatches(bookEntity -> {
                    assertNotNull(bookEntity);
                    assertNotNull(bookEntity.getId());
                    assertEquals(isbn, bookEntity.getIsbn());
                    return true;
                })
                .thenCancel()
                .verify();
    }
}
