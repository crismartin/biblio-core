package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.domain.model.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface CustomerService {
    Flux<Customer> searchByNumberMembership(String numberMembership);}
