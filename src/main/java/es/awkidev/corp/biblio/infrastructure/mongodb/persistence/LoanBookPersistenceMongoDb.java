package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.domain.persistence.BookPersistence;
import es.awkidev.corp.biblio.domain.persistence.CustomerPersistence;
import es.awkidev.corp.biblio.domain.persistence.LoanBookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.BookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CustomerReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.LoanBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.LoanBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public class LoanBookPersistenceMongoDb implements LoanBookPersistence {

    private CustomerReactive customerReactive;
    private BookReactive bookReactive;
    private LoanBookReactive loanBookReactive;

    @Autowired
    public LoanBookPersistenceMongoDb(CustomerReactive customerReactive,
                                      BookReactive bookReactive, LoanBookReactive loanBookReactive){
        this.customerReactive = customerReactive;
        this.bookReactive = bookReactive;
        this.loanBookReactive = loanBookReactive;
    }

    @Override
    public Mono<Void> create(LoanBook loanNew) {
        String numberMembership = loanNew.getNumberMembership();
        return Flux.fromStream(loanNew.getBooks().stream())
                .flatMap(book -> findBookEntity(book))
                .flatMap(loanBookEntity -> customerReactive.findCustomerEntityByNumberMembership(numberMembership)
                        .switchIfEmpty(Mono.error(new NotFoundException("Customer numberMembership: " + numberMembership)))
                        .map(customerEntity -> {
                            loanBookEntity.setCustomer(customerEntity);
                            loanBookEntity.setEndDate(LocalDate.now());
                            loanBookReactive.save(loanBookEntity);
                            return loanBookEntity;
                        }))
                .doOnNext(loanBookEntity -> this.bookReactive.save(loanBookEntity.getBook()))
                .then(Mono.empty());
    }

    private Mono<LoanBookEntity> findBookEntity(Book book){
        return bookReactive.findBookEntitiesByIsbn(book.getIsbn())
                .switchIfEmpty(Mono.error(new NotFoundException("Book isbn: " + book.getIsbn())))
                .filter(bookEntity -> bookEntity.getNumberOfCopies() > 0)
                .map(bookEntity -> {
                    bookEntity.setNumberOfCopies(bookEntity.getNumberOfCopies() - 1);
                    LoanBookEntity loanBookEntity = new LoanBookEntity();
                    loanBookEntity.setBook(bookEntity);
                    return loanBookEntity;
                });
    }

}
