package es.awkidev.corp.biblio.infrastructure.mongodb.persistence.bulkload;

import es.awkidev.corp.biblio.domain.model.*;
import es.awkidev.corp.biblio.domain.persistence.BulkLoadPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous.BookDao;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous.CopyBookDao;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CopyBookEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BulkLoadPersistenceMongoDb implements BulkLoadPersistence {

    private final BulkLoadAuthorsPersistenceMongoDb authorsPersistenceMongoDb;
    private final BulkLoadCategoriesPersistenceMongoDb categoriesPersistenceMongoDb;
    private final BookDao bookDao;
    private final CopyBookDao copyBookDao;

    @Override
    public BulkLoadSaveResult saveBooks(List<Book> books) {
        BulkLoadSaveResult saveResult = new BulkLoadSaveResult();

        log.info("Procedemos a guardar los libros: {} registros\n", books.size());

        books.stream()
                .filter(book -> checkBookIfNotExist(saveResult, book))
                .forEach(this::saveBook);

        saveResult.calcSaved(books.size());
        log.info("Se ha finalizado el proceso de guardado: {} registros guardados con exito, {} fallos al guardar\n",
                saveResult.getSaved(), saveResult.getErrors().size());

        return saveResult;
    }


    private boolean checkBookIfNotExist(BulkLoadSaveResult saveResult, Book book){
        boolean isNewBook = true;

        var bookEntityFounded = bookDao.findByIsbn(book.getIsbn());
        if(Objects.nonNull(bookEntityFounded)){
            log.warn("|_ El libro '{}' con ISBN '{}' ya esta registrado", book.getTitle(), book.getIsbn());
            saveResult.addError(
                    BulkLoadError.builder()
                            .code(BulkLoadErrorCode.CREATION.name())
                            .bookTitle(book.getTitle())
                            .message(String.format("Libro ya existente con ISBN '%s'", book.getIsbn()))
                            .build()
            );
            isNewBook = false;
        }

        return isNewBook;
    }

    private void saveBook(Book book){
        BookEntity bookEntity = new BookEntity(book);

        log.info("|_ Procedemos a guardar el libro: '{}' - ISBN '{}'", book.getTitle(), book.getIsbn());

        var authorEntities = authorsPersistenceMongoDb.saveAuthors(book);
        bookEntity.setAuthors(authorEntities);

        var categoryEntities = categoriesPersistenceMongoDb.saveCategories(book);
        bookEntity.setCategories(categoryEntities);

        var bookEntitySaved = bookDao.save(bookEntity);
        saveCopyBooks(book, bookEntitySaved);

        log.info("|_ Finalizado guardado del libro: '{}'\n", book.getTitle());
    }

    private void saveCopyBooks(Book book, BookEntity bookEntitySaved){
        var numCopies = book.getNumberOfCopies();

        if(numCopies > 0){
            log.info("  |_ Procedo a guardar {} copias del libro '{}'", numCopies, book.getTitle());

            List<CopyBookEntity> copyBookEntities =
                    Stream.iterate(0, i -> i < numCopies, i -> i + 1)
                        .map(numCopy -> {
                            var copyBookEntity = new CopyBookEntity();

                            var location = (numCopy == 0) ? CopyBookLocation.ESTANTERIA : CopyBookLocation.DEPOSITO;
                            copyBookEntity.setReference(UUID.randomUUID().toString());
                            copyBookEntity.setSignature(book.getSignature());
                            copyBookEntity.setAvailable(true);
                            copyBookEntity.setLocation(location.toString());
                            copyBookEntity.setBookEntity(bookEntitySaved);
                            return copyBookEntity;
                        })
                        .collect(Collectors.toList());

            copyBookDao.saveAll(copyBookEntities);
            log.info("  |_ Ha finalizado el guardado de copybooks del libro '{}'", book.getTitle());
        }else{
            log.warn("  |_ El libro '{}' no tiene copias para guardar", book.getTitle());
        }
    }

}
