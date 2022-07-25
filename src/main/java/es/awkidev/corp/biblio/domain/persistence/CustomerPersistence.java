package es.awkidev.corp.biblio.domain.persistence;

import es.awkidev.corp.biblio.domain.model.Customer;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerPersistence {

    Flux<Customer> searchByNumberMembership(String numberMembership);
}
