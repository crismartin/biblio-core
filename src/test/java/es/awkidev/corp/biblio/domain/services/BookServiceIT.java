package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class BookServiceIT {

    private static final String ISBN = "9788497443869";

    @Autowired
    private BookService bookService;

    @Test
    void testSearchByIsbnOk(){
        StepVerifier
                .create(bookService.searchByIsbn(ISBN))
                .expectNextMatches(book -> {
                    assertNotNull(book);
                    assertEquals(ISBN, book.getIsbn());
                    return true;
                })
                .thenCancel()
                .verify();
    }

}
