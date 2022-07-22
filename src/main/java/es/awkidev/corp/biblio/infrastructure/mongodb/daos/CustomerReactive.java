package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface CustomerReactive extends ReactiveSortingRepository<CustomerEntity, String> {

    Flux<CustomerEntity> findCustomerEntityByNumberMembership(String numberMembership);
}
