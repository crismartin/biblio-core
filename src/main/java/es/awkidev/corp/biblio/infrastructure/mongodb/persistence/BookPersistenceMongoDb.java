package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.persistence.BookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.BookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BookPersistenceMongoDb implements BookPersistence {

    private BookReactive bookReactive;

    @Autowired
    public BookPersistenceMongoDb(BookReactive bookReactive) {
        this.bookReactive = bookReactive;
    }

    @Override
    public Mono<Book> searchByIsbn(String isbn) {
        return bookReactive.findBookEntityByIsbn(isbn)
                .map(BookEntity::toBook);
    }
}
