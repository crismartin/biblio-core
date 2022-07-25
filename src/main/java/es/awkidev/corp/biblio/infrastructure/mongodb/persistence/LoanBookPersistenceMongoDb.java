package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.ConflictException;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
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
    public Mono<Boolean> create(LoanBook loanNew) {
        String numberMembership = loanNew.getNumberMembership();
        return Flux.fromStream(loanNew.getBooks().stream())
                .flatMap(this::findBookRegisteredAndAvailable)
                .flatMap(bookEntity -> findCustomerRegistered(numberMembership)
                        .map(customerEntity -> LoanBookEntity.builder()
                                .book(bookEntity)
                                .customer(customerEntity)
                                .startDate(loanNew.getStartDate())
                                .endDate(loanNew.getEndDate())
                                .build()))
                .flatMap(loanBookEntity -> this.loanBookReactive.save(loanBookEntity))
                .flatMap(this::updateNumOfCopiesBook)
                .then(Mono.just(Boolean.TRUE));
    }

    private Mono<BookEntity> findBookRegisteredAndAvailable(Book book){
        String isbn = book.getIsbn();
        return bookReactive.findBookEntityByIsbn(isbn)
                .switchIfEmpty(Mono.error(new NotFoundException("Book isbn: " + isbn)))
                .filter(bookEntity -> bookEntity.getNumberOfCopies() > 0)
                .switchIfEmpty(Mono.error(new ConflictException("Book not available: " + isbn)));
    }

    private Mono<CustomerEntity> findCustomerRegistered(String numberMembership){
        return customerReactive.findCustomerEntityByNumberMembership(numberMembership)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Customer numberMembership: " + numberMembership))
                );
                /*.flatMap(customer -> assertHasManyLoansThanLimit(customer)
                        .map(unused -> customer)
                );*/
    }

    private Mono<Void> assertHasManyLoansThanLimit(CustomerEntity customer){
        //FIXME OJO A ESTA RESTRICCION, YA QUE HABRIA QUE TENER EN CUENTA PRESTAMOS SIN ENTREGAR Y A ENTREGAR
        return loanBookReactive.findAllByCustomerAndReturnedFalse(customer)
                .collectList()
                .map(loanBookEntities -> loanBookEntities.size() == LoanBook.MAX_NUM_LOANS)
                .flatMap(hasManyLoansThanLimit -> BooleanUtils.isTrue(hasManyLoansThanLimit)
                        ? Mono.error(
                                new ConflictException("Customer has reached loan books limit. NumberMembership : "
                                        + customer.getNumberMembership())
                        )
                        : Mono.empty()
                );
    }

    private Mono<BookEntity> updateNumOfCopiesBook(LoanBookEntity loanBookEntity) {
        BookEntity bookEntity = loanBookEntity.getBook();
        bookEntity.setNumberOfCopies(bookEntity.getNumberOfCopies() - 1);
        log.info("Updated numOfCopies from book {}", bookEntity.getIsbn());
        return bookReactive.save(bookEntity);
    }
}
