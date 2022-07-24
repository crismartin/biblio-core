package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface LoanBookReactive extends ReactiveSortingRepository<LoanBookEntity, String> {

    Flux<LoanBookEntity> findAllByCustomerAndReturnedFalse(CustomerEntity customer);
}
