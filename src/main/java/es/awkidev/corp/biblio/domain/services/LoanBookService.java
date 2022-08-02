package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.domain.model.LoanBook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface LoanBookService {
    Mono<LoanBook> create(LoanBook loanNew);

    Mono<LoanBook> findByReference(String reference);
}
