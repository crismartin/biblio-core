package es.awkidev.corp.biblio.domain.persistence;

import es.awkidev.corp.biblio.domain.model.Book;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookPersistence {
    Mono<Book> searchByIsbn(String isbn);
}
