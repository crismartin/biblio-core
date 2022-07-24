package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface BookReactive extends ReactiveSortingRepository<BookEntity, String> {
    Mono<BookEntity> findBookEntitiesByIsbn(String isbn);
}
