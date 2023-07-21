package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.domain.persistence.LoanBookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CustomerReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LoanBookPersistenceMongoDb implements LoanBookPersistence {

    private final CustomerReactive customerReactive;
    private final LoanBookReactive loanBookReactive;
    private final LoanCreationPersistenceMongoDb loanCreationPersistenceMongoDb;

    @Override
    public Mono<LoanBook> create(LoanBook loanNew) {
        return loanCreationPersistenceMongoDb.create(loanNew);
    }

    @Override
    public Mono<LoanBook> findByReference(String reference) {
        LoanBook loanBook = new LoanBook();
        log.info("Find loan by reference '{}'", reference);

        return loanBookReactive.findFirstByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Loan reference: " + reference)))
                .flatMap(loanBookEntity ->
                        customerReactive.findCustomerEntityByNumberMembership(loanBookEntity.getNumberMembership())
                        .map(customerEntity -> {
                            loanBook.setReference(reference);
                            loanBook.setEndDate(loanBookEntity.getEndDate());
                            loanBook.setCustomer(customerEntity.toCustomer());
                            return loanBook;
                        })
                );
    }
}
