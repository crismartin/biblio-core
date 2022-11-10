package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class CopyBookServiceIT {

    private static final String REFERENCE = "ref-5";
    private static final String BOOK_ISBN = "9788497443869";

    @Autowired
    private CopyBookService copyBookService;

    @Test
    void testSearchByBookIsbnAvailableOk(){
        StepVerifier
                .create(copyBookService.searchByBookIsbnAvailable(BOOK_ISBN))
                .expectNextMatches(copyBook -> {
                    assertNotNull(copyBook);
                    assertNotNull(copyBook.getBook());
                    assertEquals(BOOK_ISBN, copyBook.getBookIsbn());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testGetByReferenceOk(){
        StepVerifier
                .create(copyBookService.getByReference(REFERENCE))
                .expectNextMatches(copyBook -> {
                    assertNotNull(copyBook);
                    assertNotNull(copyBook.getBook());
                    assertEquals(REFERENCE, copyBook.getReference());
                    return true;
                })
                .thenCancel()
                .verify();
    }
}
