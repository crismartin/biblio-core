package es.awkidev.corp.biblio.domain.persistence;

import es.awkidev.corp.biblio.domain.model.LoanBook;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LoanBookPersistence {

    Mono<Boolean> create(LoanBook loanNew);
}
