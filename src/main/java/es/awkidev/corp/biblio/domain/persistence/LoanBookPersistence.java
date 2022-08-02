package es.awkidev.corp.biblio.domain.persistence;

import es.awkidev.corp.biblio.domain.model.LoanBook;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LoanBookPersistence {

    Mono<LoanBook> create(LoanBook loanNew);

    Mono<LoanBook> findByReference(String reference);
}
