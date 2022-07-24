package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface LoanBookReactive extends ReactiveSortingRepository<LoanBookEntity, String> {
}
