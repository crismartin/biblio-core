package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.domain.model.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CustomerService {
    Flux<String> searchByNumberMembership(String numberMembership);
    Mono<Customer> findByNumberMembership(String numberMembership);
}
