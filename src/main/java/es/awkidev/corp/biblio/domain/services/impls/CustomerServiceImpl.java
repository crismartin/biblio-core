package es.awkidev.corp.biblio.domain.services.impls;

import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.persistence.CustomerPersistence;
import es.awkidev.corp.biblio.domain.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerPersistence customerPersistence;

    @Autowired
    public CustomerServiceImpl(CustomerPersistence customerPersistence){
        this.customerPersistence = customerPersistence;
    }

    @Override
    public Flux<String> searchByNumberMembership(String numberMembership) {
        return customerPersistence.searchByNumberMembership(numberMembership)
                .map(customer -> customer.getNumberMembership() + " - "
                        + customer.getFullName()
                );
    }

    @Override
    public Mono<Customer> findByNumberMembership(String numberMembership) {
        return customerPersistence.findByNumberMembership(numberMembership);
    }
}
