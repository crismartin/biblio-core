package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface CopyBookReactive extends ReactiveSortingRepository<CopyBookEntity, String> {
    Mono<CopyBookEntity> findByReferenceAndAvailableTrue(String reference);
}
