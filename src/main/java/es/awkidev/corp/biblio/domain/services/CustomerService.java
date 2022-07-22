package es.awkidev.corp.biblio.domain.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface CustomerService {
    Flux<String> searchByNumberMembership(String numberMembership);
}
