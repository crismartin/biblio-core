package es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CopyBookDao extends MongoRepository<CopyBookEntity, String> {
}
