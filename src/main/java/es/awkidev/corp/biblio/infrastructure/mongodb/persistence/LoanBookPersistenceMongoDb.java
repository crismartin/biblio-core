package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.domain.persistence.LoanBookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CustomerReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class LoanBookPersistenceMongoDb implements LoanBookPersistence {

    private CustomerReactive customerReactive;
    private LoanBookReactive loanBookReactive;
    private LoanCreationPersistenceMongoDb loanCreationPersistenceMongoDb;

    @Autowired
    public LoanBookPersistenceMongoDb(CustomerReactive customerReactive,
                                      LoanBookReactive loanBookReactive,
                                      LoanCreationPersistenceMongoDb loanCreationPersistenceMongoDb){
        this.customerReactive = customerReactive;
        this.loanBookReactive = loanBookReactive;
        this.loanCreationPersistenceMongoDb = loanCreationPersistenceMongoDb;
    }

    @Override
    public Mono<LoanBook> create(LoanBook loanNew) {
        return loanCreationPersistenceMongoDb.create(loanNew);
    }

    @Override
    public Mono<LoanBook> findByReference(String reference) {
        LoanBook loanBook = new LoanBook();
        log.info("Find loan by reference '{}'", reference);

        return loanBookReactive.findByReference(reference)
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
