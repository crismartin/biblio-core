package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.ConflictException;
import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CopyBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CustomerReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.PenalizationReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanCreationValidationMongoDb {

    private final CustomerReactive customerReactive;
    private final CopyBookReactive copyBookReactive;
    private final PenalizationReactive penalizationReactive;
    private final LoanBookReactive loanBookReactive;


    public Mono<List<LoanBookEntity>> checkLoanValid(LoanBook loanNew){
        AtomicReference<LoanBookEntity> loanBookAtomic = new AtomicReference<>();

        return customerValidation(loanNew.getCustomerNumberMembership(), loanNew.getCopyBooks().size())
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
                .flatMap(unused -> findCopyBooksAvailable(loanNew.getCopyBooks())
                        .map(copyBookEntities ->
                             buildLoanBookEntities(loanBookAtomic.get(), copyBookEntities)
                        )
                );
    }

    private List<LoanBookEntity> buildLoanBookEntities(LoanBookEntity loanBookAtomic, List<CopyBookEntity> copyBookEntities) {
        return !CollectionUtils.isEmpty(copyBookEntities)
                ? copyBookEntities.stream()
                .map(copyBookEntity ->
                        LoanBookEntity.builder()
                                .reference(loanBookAtomic.getReference())
                                .customerEntity(loanBookAtomic.getCustomerEntity())
                                .copyBookEntity(copyBookEntity)
                                .returned(false)
                                .startDate(loanBookAtomic.getStartDate())
                                .endDate(loanBookAtomic.getEndDate())
                                .build())
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    private Mono<CustomerEntity> customerValidation(String customerNumberMembership, int numBooksLoan) {
        return findCustomerRegistered(customerNumberMembership)
                .flatMap(this::checkNoPenalizationCustomer)
                .flatMap(customer -> checkMaxLoansCustomerEntity(customer, numBooksLoan));
    }

    private Mono<CustomerEntity> findCustomerRegistered(String numberMembership){
        log.info("Find if customer with membership '{}' is registered", numberMembership);

        return customerReactive.findCustomerEntityByNumberMembership(numberMembership)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer numberMembership: " + numberMembership)));
    }

    private Mono<CustomerEntity> checkNoPenalizationCustomer(CustomerEntity customerEntity){
        log.info("Check customer has not penalization by membership '{}'", customerEntity.getNumberMembership());

        return penalizationReactive.findFirstByCustomerEntityAndActiveTrue(customerEntity)
                .flatMap(penalizationEntity -> Mono.error(
                        new ConflictException("Customer has at least one penalization. " +
                                "NumberMembership : " + customerEntity.getNumberMembership())))
                .thenReturn(customerEntity);
    }

    private Mono<CustomerEntity> checkMaxLoansCustomerEntity(CustomerEntity customerEntity, int numBooksLoan){
        log.info("Check max loans customer by membership '{}'", customerEntity.getNumberMembership());

        return loanBookReactive.countAllByCustomerEntityAndReturnedFalseAndEndDateIsGreaterThanEqual(customerEntity, LocalDate.now())
                .filter(numLoans -> (int) (numLoans + numBooksLoan) <= LoanBook.MAX_NUM_LOANS)
                .switchIfEmpty(Mono.error(new ConflictException("Customer could have or has reached loan books limit. " +
                        "NumberMembership : " + customerEntity.getNumberMembership())))
                .map(unused -> customerEntity);
    }

    private Mono<List<CopyBookEntity>> findCopyBooksAvailable(List<CopyBook> copyBooks){
        log.info("Find if copybooks references are available");

        return Flux.fromStream(copyBooks.stream())
                .flatMap(copyBook -> copyBookReactive.findByReferenceAndAvailableTrue(copyBook.getReference())
                        .switchIfEmpty(Mono.error(new NotFoundException("CopyBook reference: " + copyBook.getReference())))
                )
                .collectList()
                .filter(copyBookEntities -> !copyBookEntities.isEmpty())
                .switchIfEmpty(Mono.error(new NotFoundException("Copybooks references are empty")));
    }

}