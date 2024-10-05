package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.TestConfig;
import es.awkidev.corp.biblio.domain.model.SearchBookFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class BookPersistenceMongoDbIT {

    @Autowired
    private BookPersistenceMongoDb bookPersistenceMongoDb;

    @Test
    void testSearchByIsbnOk(){
        String isbn = "9788497443869";

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

    @Test
    void testSearchBookByFilterOnlyKeyword(){
        SearchBookFilter filter = new SearchBookFilter();
        filter.setKeyword("libro");
        filter.setAuthorFullName(StringUtils.EMPTY);

        StepVerifier
                .create(this.bookPersistenceMongoDb.searchBooksByFilter(filter))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .expectRecordedMatches(elements ->  !elements.isEmpty() && elements.size() == 2)
                .verifyComplete();
    }

    @Test
    void testSearchBookByFilterOnlyAuthor(){
        SearchBookFilter filter = new SearchBookFilter();
        filter.setKeyword("");
        filter.setAuthorFullName("AMADOR RIVAS");

        StepVerifier
                .create(this.bookPersistenceMongoDb.searchBooksByFilter(filter))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .expectRecordedMatches(elements ->  !elements.isEmpty() && elements.size() == 2)
                .verifyComplete();
    }

    @Test
    void testSearchBookByFilter(){
        SearchBookFilter filter = new SearchBookFilter();
        filter.setKeyword("libro");
        filter.setAuthorFullName("AMADOR RIVAS");

        StepVerifier
                .create(this.bookPersistenceMongoDb.searchBooksByFilter(filter))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .expectRecordedMatches(elements ->  !elements.isEmpty() && elements.size() == 1)
                .verifyComplete();
    }
}
