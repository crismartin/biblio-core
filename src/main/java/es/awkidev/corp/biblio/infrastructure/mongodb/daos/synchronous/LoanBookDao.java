package es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoanBookDao extends MongoRepository<LoanBookEntity, String> {
}
