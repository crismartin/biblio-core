package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.model.SearchBookFilter;
import es.awkidev.corp.biblio.domain.persistence.BookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.AuthorReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.BookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CopyBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.AuthorEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Repository
public class BookPersistenceMongoDb implements BookPersistence {

    private final BookReactive bookReactive;

    private final AuthorReactive authorReactive;

    private CopyBookReactive copyBookReactive;

    private LoanBookReactive loanBookReactive;


    @Override
    public Mono<Book> searchByIsbn(String isbn) {
        return bookReactive.findBookEntityByIsbn(isbn)
                .map(BookEntity::toBook);
    }

    @Override
    public Flux<Book> searchBooksByFilter(SearchBookFilter filter) {

        Mono<AuthorEntity> monoAuthorEntity = Mono.just(StringUtils.defaultIfBlank(filter.getAuthorFullName(), StringUtils.EMPTY).toUpperCase())
                .filter(StringUtils::isNotBlank)
                .flatMap(authorReactive::findFirstByFullName)
                .switchIfEmpty(Mono.just(new AuthorEntity()));

        return monoAuthorEntity
                .flatMapMany(authorEntity -> StringUtils.isNoneBlank(authorEntity.getId())
                        ? bookReactive.findAllByTitleContainsAndAuthorsIn(filter.getKeyword().toUpperCase(), List.of(authorEntity))
                        : bookReactive.findAllByTitleContains(filter.getKeyword().toUpperCase()))
                .map(BookEntity::toBook);
    }

    @Override
    public Mono<Book> getBookByIsbn(String isbn) {
        return bookReactive.findBookEntityByIsbn(isbn)
                .switchIfEmpty(Mono.error(new NotFoundException("Book isbn: " + isbn)))
                .flatMap(bookEntity ->
                        Mono.zip(
                                getNumberOfCopiesAvailablesFromBookEntity(bookEntity),
                                getNextAvailabilityFromBookEntity(bookEntity))
                        .map(tuple -> {
                            var book = bookEntity.toBook();
                            book.setNumberOfCopies(tuple.getT1());

                            if(book.getNumberOfCopies() == 0){
                                book.setNearAvailabilityDate(tuple.getT2().getAvailabilityDate());
                            }
                            return book;
                        })
                );
    }

    public Mono<Integer> getNumberOfCopiesAvailablesFromBookEntity(BookEntity bookEntity){
        return copyBookReactive.findAllByBookEntityAndAvailableTrue(bookEntity)
                .count()
                .map(numberCopies -> {
                    log.info("Has {} number of copies availability of book {}", numberCopies, bookEntity.getIsbn());
                    return Integer.valueOf(numberCopies.toString());
                });
    }

    public Mono<CopyBook> getNextAvailabilityFromBookEntity(BookEntity bookEntity){
        return copyBookReactive.findAllByBookEntityAndAvailableFalse(bookEntity)
                .flatMap(copyBookEntity -> loanBookReactive.findByCopyBookEntity(copyBookEntity)
                        .map(loanBookEntity -> {
                            var copyBook = loanBookEntity.getCopyBookEntity().toCopyBook();
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

}
