package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.AuthorEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface AuthorReactive extends ReactiveSortingRepository<AuthorEntity, String> {

    Mono<AuthorEntity> findFirstByReference(String referenceAuthor);
}