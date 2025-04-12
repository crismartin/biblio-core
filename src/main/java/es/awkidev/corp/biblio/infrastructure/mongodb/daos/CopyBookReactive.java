package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CopyBookReactive extends ReactiveSortingRepository<CopyBookEntity, String> {

    Mono<CopyBookEntity> findByReferenceAndAvailableTrue(String reference);

    Mono<CopyBookEntity> findFirstByBookEntityAndAvailableTrue(BookEntity bookEntity);

    Flux<CopyBookEntity> findAllByBookEntityAndAvailableFalse(BookEntity bookEntity);

    Flux<CopyBookEntity> findAllByBookEntityAndAvailableTrue(BookEntity bookEntity);
}
