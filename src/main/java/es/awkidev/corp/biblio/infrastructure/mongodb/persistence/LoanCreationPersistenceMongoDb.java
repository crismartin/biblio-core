package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CopyBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LoanCreationPersistenceMongoDb {

    private final CopyBookReactive copyBookReactive;
    private final LoanBookReactive loanBookReactive;
    private final LoanCreationValidationMongoDb loanCreationValidationMongoDb;

    public Mono<LoanBook> create(LoanBook loanNew) {
        AtomicReference<LoanBook> loanBookAtomicResult = new AtomicReference<>();

        return loanCreationValidationMongoDb.checkLoanValid(loanNew)
                .flatMap(loanBookEntities -> this.loanBookReactive.saveAll(loanBookEntities)
                        .collectList()
                        .map(loanBooks -> {
                            LoanBook result = new LoanBook();
                            result.setCustomer(loanBooks.get(0).getCustomerEntity().toCustomer());
                            result.setCopyBooks(loanBooks.stream()
                                    .map(LoanBookEntity::getCopyBookEntity)
                                    .map(CopyBookEntity::toCopyBook)
                                    .collect(Collectors.toList())
                            );
                            result.setReference(loanNew.getReference());
                            result.setStartDate(loanNew.getStartDate());
                            result.setEndDate(loanNew.getEndDate());
                            loanBookAtomicResult.set(result);
                            return loanBooks.stream()
                                    .map(LoanBookEntity::getCopyBookEntity)
                                    .collect(Collectors.toList());
                        })
                )
                .flatMap(copyBookEntities -> updateAvailabilityCopyBookEntities(copyBookEntities, loanNew.getReference()))
                .map(unused -> loanBookAtomicResult.get());
    }

    private Mono<List<CopyBookEntity>> updateAvailabilityCopyBookEntities(List<CopyBookEntity> copyBookEntities,
                                                                          String loanReference) {
        log.info("Updating copybookEntities to loan reference '{}'", loanReference);

        var copyBookList = copyBookEntities.stream()
                .peek(copyBookEntity -> {
                    copyBookEntity.setAvailable(false);
                    log.info("Updated copyBookEntity with reference {}", copyBookEntity.getReference());
                })
                .collect(Collectors.toList());
        return copyBookReactive.saveAll(copyBookList)
                .collectList();
    }

}
