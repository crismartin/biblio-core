package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.ConflictException;
import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.domain.persistence.LoanBookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CopyBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CustomerReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@Slf4j
public class LoanBookPersistenceMongoDb implements LoanBookPersistence {

    private CustomerReactive customerReactive;
    private CopyBookReactive copyBookReactive;
    private LoanBookReactive loanBookReactive;

    @Autowired
    public LoanBookPersistenceMongoDb(CustomerReactive customerReactive,
                                      LoanBookReactive loanBookReactive,
                                      CopyBookReactive copyBookReactive){
        this.customerReactive = customerReactive;
        this.loanBookReactive = loanBookReactive;
        this.copyBookReactive = copyBookReactive;
    }

    @Override
    public Mono<LoanBook> create(LoanBook loanNew) {
        String numberMembership = loanNew.getNumberMembership();
        return Flux.fromStream(loanNew.getCopyBooks().stream())
                .flatMap(this::findCopyBookAvailable)
                .flatMap(copyBookEntity -> findCustomerRegistered(numberMembership)
                        .map(customerEntity -> LoanBookEntity.builder()
                                .returned(false)
                                .copyBookEntity(copyBookEntity)
                                .customerEntity(customerEntity)
                                .reference(loanNew.getReference())
                                .startDate(loanNew.getStartDate())
                                .endDate(loanNew.getEndDate())
                                .build()))
                .flatMap(loanBookEntity -> {
                    log.info("loanBookEntity to save: {}", loanBookEntity);
                    return loanBookReactive.save(loanBookEntity);
                })
                .flatMap(loanBookEntity ->
                        updateAvailabilityCopyBook(loanBookEntity.getCopyBookEntity()))
                .collectList()
                .map(unused -> loanNew);
    }

    private Mono<CopyBookEntity> findCopyBookAvailable(CopyBook copyBook){
        String copyBookReference = copyBook.getReference();
        log.info("Find if copybook  with reference '{}' is available", copyBookReference);

        return copyBookReactive.findByReferenceAndAvailableTrue(copyBookReference)
                .switchIfEmpty(Mono.error(new NotFoundException("CopyBook reference: " + copyBookReference)));
    }

    private Mono<CustomerEntity> findCustomerRegistered(String numberMembership){
        log.info("Find if customer with membership '{}' is registered", numberMembership);

        return customerReactive.findCustomerEntityByNumberMembership(numberMembership)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer numberMembership: " + numberMembership)))
                .flatMap(customer -> assertHasReachedLoansLimit(customer)
                        .map(unused -> customer)
                )
                .flatMap(customer -> assertHasPendingBooksOutLimitDate(customer)
                        .map(unused -> customer)
                );
    }

    private Mono<?> assertHasReachedLoansLimit(CustomerEntity customer){
        log.info("Check if customer with membership '{}' has reached loans limit",
                customer.getNumberMembership());

        return loanBookReactive.findAllByCustomerEntityAndReturnedFalse(customer)
                .collectList()
                .map(loanBookEntities ->
                        (loanBookEntities.size() == LoanBook.MAX_NUM_LOANS)
                        ? Mono.error(
                                new ConflictException("Customer has reached loan books limit. NumberMembership : "
                                + customer.getNumberMembership())
                        )
                        : Mono.empty());
    }

    private Mono<?> assertHasPendingBooksOutLimitDate(CustomerEntity customer){
        log.info("Check if customer with membership '{}' has pending return out",
                customer.getNumberMembership());

        return loanBookReactive.findAllByCustomerEntityAndReturnedFalse(customer)
                .collectList()
                .map(loanBookEntities ->
                        (loanBookEntities.isEmpty())
                        ? Mono.error(new ConflictException("Customer has books that have not yet been returned. " +
                                "NumberMembership : " + customer.getNumberMembership()))
                        : Mono.empty()
                );
    }

    private Mono<CopyBookEntity> updateAvailabilityCopyBook(CopyBookEntity copyBookEntity) {
        log.info("Updated copybook with reference: {}", copyBookEntity.getReference());

        copyBookEntity.setAvailable(false);
        return copyBookReactive.save(copyBookEntity);
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
                            loanBook.setEndDate(loanBookEntity.getEndDate());
                            loanBook.setCustomer(customerEntity.toCustomer());
                            return loanBookEntity;
                        })
                )
                .flatMap(loanBookEntity ->
                        copyBookReactive.findByReference(loanBookEntity.getCopyBookEntity().getReference())
                        .map(copyBookEntity -> {
                            loanBook.setCopyBooks(List.of(copyBookEntity.toCopyBook()));
                            return loanBook;
                        })
                );
    }
}
