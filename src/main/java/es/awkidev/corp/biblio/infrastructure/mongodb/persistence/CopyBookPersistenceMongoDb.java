package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.exceptions.NotFoundException;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.persistence.CopyBookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.BookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.CopyBookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CopyBookPersistenceMongoDb implements CopyBookPersistence {

    private BookReactive bookReactive;
    private CopyBookReactive copyBookReactive;

    @Autowired
    public CopyBookPersistenceMongoDb(CopyBookReactive copyBookReactive, BookReactive bookReactive){
        this.copyBookReactive = copyBookReactive;
        this.bookReactive = bookReactive;
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
