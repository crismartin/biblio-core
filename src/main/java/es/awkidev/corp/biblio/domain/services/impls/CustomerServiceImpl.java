package es.awkidev.corp.biblio.domain.services.impls;

import es.awkidev.corp.biblio.domain.model.Customer;
import es.awkidev.corp.biblio.domain.persistence.CustomerPersistence;
import es.awkidev.corp.biblio.domain.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerPersistence customerPersistence;

    @Autowired
    public CustomerServiceImpl(CustomerPersistence customerPersistence){
        this.customerPersistence = customerPersistence;
    }

    @Override
    public Flux<Customer> searchByNumberMembership(String numberMembership) {
        return customerPersistence.searchByNumberMembership(numberMembership);
    }
}
