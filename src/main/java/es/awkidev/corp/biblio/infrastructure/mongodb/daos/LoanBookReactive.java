package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface LoanBookReactive extends ReactiveSortingRepository<LoanBookEntity, String> {

    Flux<LoanBookEntity> findAllByCustomerEntityAndReturnedFalse(CustomerEntity customerEntity);

    Mono<Long> countAllByCustomerEntityAndReturnedFalseAndEndDateIsGreaterThanEqual(CustomerEntity customerEntity, LocalDate currentDay);

    Mono<LoanBookEntity> findFirstByReference(String reference);
}
