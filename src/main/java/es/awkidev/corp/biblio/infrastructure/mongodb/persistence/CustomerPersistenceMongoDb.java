package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.persistence.CustomerPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CustomerReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.PenalizationReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.PenalizationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class CustomerPersistenceMongoDb implements CustomerPersistence {

    private final CustomerReactive customerReactive;

    private final PenalizationReactive penalizationReactive;

    @Override
    public Flux<Customer> searchByNumberMembership(String numberMembership) {
        return this.customerReactive.findAllByNumberMembership(numberMembership)
                .flatMap(customerEntity -> {
                    Customer customer = customerEntity.toCustomer();
                    return penalizationReactive.existsPenalizationEntityByCustomerEntityAndActiveTrue(customerEntity)
                            .map(penalization -> {
                                customer.setHasPenalization(penalization);
                                return customer;
                            });
                });
    }
}
