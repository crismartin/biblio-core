package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.persistence.CopyBookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.BookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CopyBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Repository
public class CopyBookPersistenceMongoDb implements CopyBookPersistence {

    private BookReactive bookReactive;
    private CopyBookReactive copyBookReactive;
    private LoanBookReactive loanBookReactive;

    @Autowired
    public CopyBookPersistenceMongoDb(CopyBookReactive copyBookReactive, BookReactive bookReactive, LoanBookReactive loanBookReactive){
        this.copyBookReactive = copyBookReactive;
        this.bookReactive = bookReactive;
        this.loanBookReactive = loanBookReactive;
    }

    @Override
    public Mono<CopyBook> searchByBookIsbnAvailable(String isbn) {
        return bookReactive.findBookEntityByIsbn(isbn)
                .switchIfEmpty(Mono.error(new NotFoundException("Book isbn: " + isbn)))
                .flatMap(bookEntity -> copyBookReactive.findFirstByBookEntityAndAvailableTrue(bookEntity)
                        .map(CopyBookEntity::toCopyBook)
                        .map(copyBook -> {
                            Book book = bookEntity.toBook();
                            copyBook.setBook(book);
                            return copyBook;
                        })
                );
    }

    @Override
    public Mono<CopyBook> getCopybookFromIsbn(String isbn){
        return bookReactive.findBookEntityByIsbn(isbn)
                .switchIfEmpty(Mono.error(new NotFoundException("Book isbn: " + isbn)))
                .flatMap(bookEntity ->
                        Mono.zip(
                                getAvailableOrNextAvailabilityFromBookEntity(bookEntity),
                                getNumberOfCopiesAvailablesFromBookEntity(bookEntity))
                        .map(tuple -> {
                            tuple.getT1().getBook().setNumberOfCopies(tuple.getT2());
                            return tuple.getT1();
                        })
                );
    }

    public Mono<Integer> getNumberOfCopiesAvailablesFromBookEntity(BookEntity bookEntity){
        return copyBookReactive.findAllByBookEntityAndAvailableTrue(bookEntity)
                .count()
                .map(numberCopies -> Integer.valueOf(numberCopies.toString()));
    }

    public Mono<CopyBook> getAvailableOrNextAvailabilityFromBookEntity(BookEntity bookEntity){
        return copyBookReactive.findFirstByBookEntityAndAvailableTrue(bookEntity)
                .flatMap(copyBookEntityAvailable -> {
                    Mono<CopyBook> result;
                    if (copyBookEntityAvailable != null) {
                        log.info("Has a copybook available from book {}", bookEntity.getIsbn());
                        var copyBook = copyBookEntityAvailable.toCopyBook();
                        copyBook.setBook(bookEntity.toBook());
                        result = Mono.just(copyBook);
                    } else {
                        log.info("Havent an available copybooks from book {}", bookEntity.getIsbn());
                        result = copyBookReactive.findAllByBookEntityAndAvailableFalse(bookEntity)
                                .flatMap(copyBookEntity -> loanBookReactive.findByCopyBookEntity(copyBookEntity)
                                        .map(loanBookEntity -> {
                                            copyBookEntityAvailable.setBookEntity(bookEntity);
                                            var copyBook = loanBookEntity.getCopyBookEntity().toCopyBook();
                                            copyBook.setBook(bookEntity.toBook());
                                            copyBook.setAvailabilityDate(loanBookEntity.getEndDate());
                                            return copyBook;
                                        })
                                )
                                .reduce((copyBookA, copyBookB) ->
                                        copyBookA.getAvailabilityDate().isBefore(copyBookB.getAvailabilityDate())
                                                ? copyBookA
                                                : copyBookB
                                );
                    }
                    return result;
                });
    }

    @Override
    public Mono<CopyBook> getByReference(String reference) {
        return copyBookReactive.findByReferenceAndAvailableTrue(reference)
                .flatMap(copyBookEntity -> bookReactive.findById(copyBookEntity.getBookEntity().getId())
                        .map(bookEntity -> {
                            CopyBook copyBook = copyBookEntity.toCopyBook();
                            copyBook.setBook(bookEntity.toBook());
                            return copyBook;
                        })
                );
    }

}
