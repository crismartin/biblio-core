package es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.AuthorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorDao extends MongoRepository<AuthorEntity, String> {
}
