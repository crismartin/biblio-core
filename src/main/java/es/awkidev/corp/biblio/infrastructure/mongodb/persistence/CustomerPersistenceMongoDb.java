package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.persistence.CustomerPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CustomerReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerPersistenceMongoDb implements CustomerPersistence {

    private CustomerReactive customerReactive;

    @Autowired
    public CustomerPersistenceMongoDb(CustomerReactive customerReactive){
        this.customerReactive = customerReactive;
    }

    @Override
    public Flux<Customer> searchByNumberMembership(String numberMembership) {
        return this.customerReactive.findAllByNumberMembership(numberMembership)
                .map(CustomerEntity::mapToCustomer);
    }

    @Override
    public Mono<Customer> findByNumberMembership(String numberMembership) {
        return this.customerReactive.findCustomerEntityByNumberMembership(numberMembership)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer numberMembership: " + numberMembership)))
                .map(CustomerEntity::mapToCustomer);
    }
}
