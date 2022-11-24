package es.awkidev.corp.biblio.domain.services.impls;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.persistence.BookPersistence;
import es.awkidev.corp.biblio.domain.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BookServiceImpl implements BookService {

    private BookPersistence bookPersistence;

    @Autowired
    public BookServiceImpl(BookPersistence bookPersistence){
        this.bookPersistence = bookPersistence;
    }

    @Override
    public Mono<Book> searchByIsbn(String isbn) {
        return bookPersistence.searchByIsbn(isbn);
    }
}
