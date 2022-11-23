package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.ConflictException;
import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.model.LoanBook;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class LoanCreationPersistenceMongoDb {

    private CustomerReactive customerReactive;
    private CopyBookReactive copyBookReactive;
    private LoanBookReactive loanBookReactive;

    @Autowired
    public LoanCreationPersistenceMongoDb(CustomerReactive customerReactive,
                                          LoanBookReactive loanBookReactive,
                                          CopyBookReactive copyBookReactive){
        this.customerReactive = customerReactive;
        this.loanBookReactive = loanBookReactive;
        this.copyBookReactive = copyBookReactive;
    }

    public Mono<LoanBook> create(LoanBook loanNew) {
        String numberMembership = loanNew.getNumberMembership();
        AtomicReference<LoanBookEntity> loanBookAtomic = new AtomicReference<>();

        return findCustomerRegistered(numberMembership)
                .doOnNext(customerEntity -> {
                    LoanBookEntity loanBookEntity = LoanBookEntity.builder()
                            .returned(false)
                            .customerEntity(customerEntity)
                            .reference(loanNew.getReference())
                            .startDate(loanNew.getStartDate())
                            .endDate(loanNew.getEndDate())
                            .build();
                    loanBookAtomic.set(loanBookEntity);
                })
                .flatMap(unused -> findCopyBookAvailable(loanNew.getCopyBooks()))
                .doOnNext(copyBookEntities -> loanBookAtomic.get().setCopyBookEntities(copyBookEntities))
                .flatMap(unused -> loanBookReactive.save(loanBookAtomic.get())
                        .map(LoanBookEntity::getCopyBookEntities)
                )
                .flatMap(this::updateAvailabilityCopyBookEntities)
                .map(unused -> loanBookAtomic.get())
                .map(LoanBookEntity::toLoanBook);

    }

    private Mono<List<CopyBookEntity>> findCopyBookAvailable(List<CopyBook> copyBooks){
        log.info("Find if copybooks references are available");

        return Flux.fromStream(copyBooks.stream())
                .flatMap(copyBook -> copyBookReactive.findByReferenceAndAvailableTrue(copyBook.getReference())
                        .switchIfEmpty(Mono.error(new NotFoundException("CopyBook reference: " + copyBook.getReference())))
                )
                .collectList()
                .filter(copyBookEntities -> !copyBookEntities.isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException("Copybooks references are empty")));
    }

    private Mono<CustomerEntity> findCustomerRegistered(String numberMembership){
        log.info("Find if customer with membership '{}' is registered", numberMembership);

        return customerReactive.findCustomerEntityByNumberMembership(numberMembership)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer numberMembership: " + numberMembership)))
                .flatMap(this::checkMaxLoansCustomerEntity);
    }

    private Mono<CustomerEntity> checkMaxLoansCustomerEntity(CustomerEntity customerEntity){
        log.info("Check max loans customer by membership '{}'", customerEntity.getNumberMembership());

        return loanBookReactive.findAllByCustomerEntityAndReturnedFalse(customerEntity)
                .collectList()
                .filter(loanBookEntities -> getTotalCopyBooksOfLoans(loanBookEntities) < LoanBook.MAX_NUM_LOANS)
                .switchIfEmpty(Mono.error(new ConflictException("Customer has reached loan books limit. " +
                        "NumberMembership : " + customerEntity.getNumberMembership())))
                .map(unused -> customerEntity);
    }

    private int getTotalCopyBooksOfLoans(List<LoanBookEntity> loanBookEntities){
        log.info("Get total copybooks of loans");

        return loanBookEntities.stream()
                .map(LoanBookEntity::getCopyBookEntities)
                .reduce((acumulator, loanBookEntity) -> {
                    acumulator.addAll(loanBookEntity);
                    return acumulator;
                })
                .map(List::size)
                .orElse(0);
    }

    private Mono<List<CopyBookEntity>> updateAvailabilityCopyBookEntities(List<CopyBookEntity> copyBookEntities) {
        log.info("Updating copybookEntities");

        var copyBookList = copyBookEntities.stream()
                .map(copyBookEntity -> {
                    copyBookEntity.setAvailable(false);
                    log.info("Updated copyBookEntity with reference {}", copyBookEntity.getReference());
                    return copyBookEntity;
                })
                .collect(Collectors.toList());
        return copyBookReactive.saveAll(copyBookList)
                .collectList();
    }

}
