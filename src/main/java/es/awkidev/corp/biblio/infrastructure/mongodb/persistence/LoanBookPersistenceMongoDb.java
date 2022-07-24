package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.LoanBook;
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
                .flatMap(this::findBookRegistered)
                .flatMap(bookEntity -> findCustomerRegistered(numberMembership)
                        .map(customerEntity -> LoanBookEntity.builder()
                                .book(bookEntity)
                                .customer(customerEntity)
                                .endDate(LocalDate.now())
                                .build()))
                .doOnNext(loanBookEntity -> this.loanBookReactive.save(loanBookEntity))
                .then(Mono.empty());
    }

    private Mono<BookEntity> findBookRegistered(Book book){
        return bookReactive.findBookEntitiesByIsbn(book.getIsbn())
                .switchIfEmpty(Mono.error(new NotFoundException("Book isbn: " + book.getIsbn())))
                .filter(bookEntity -> bookEntity.getNumberOfCopies() > 0)
                .map(this::updateNumOfCopiesBook);
    }

    private BookEntity updateNumOfCopiesBook(BookEntity bookEntity){
        bookEntity.setNumberOfCopies(bookEntity.getNumberOfCopies() - 1);
        this.bookReactive.save(bookEntity);
        return bookEntity;
    }

    private Mono<CustomerEntity> findCustomerRegistered(String numberMembership){
        return customerReactive.findCustomerEntityByNumberMembership(numberMembership)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer numberMembership: " + numberMembership)));
    }

}
