package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.AuthorEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookReactive extends ReactiveSortingRepository<BookEntity, String> {
    Mono<BookEntity> findBookEntityByIsbn(String isbn);

    Flux<BookEntity> findAllByTitleContainsAndAuthorsIn(String keyword, List<AuthorEntity> authors);

    Flux<BookEntity> findAllByTitleContains(String keyword);
}
