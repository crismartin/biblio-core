package es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.PublisherEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PublisherDao extends MongoRepository<PublisherEntity, String> {
}
