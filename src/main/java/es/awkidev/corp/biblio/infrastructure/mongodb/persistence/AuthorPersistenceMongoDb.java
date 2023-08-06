package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.model.Author;
import es.awkidev.corp.biblio.domain.persistence.AuthorPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.AuthorReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.AuthorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AuthorPersistenceMongoDb implements AuthorPersistence {

    private final AuthorReactive authorReactive;

    @Override
    public Flux<Author> searchByFullName(String fullName) {
        return authorReactive.findAllByFullNameContains(fullName)
                .map(AuthorEntity::toAuthor);
    }
}
