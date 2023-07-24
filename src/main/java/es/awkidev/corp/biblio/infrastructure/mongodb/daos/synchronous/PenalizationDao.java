package es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.PenalizationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PenalizationDao extends MongoRepository<PenalizationEntity, String> {
}
