package es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookDao extends MongoRepository<BookEntity, String> {
}
