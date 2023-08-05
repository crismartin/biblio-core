package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.SearchBookFilter;
import es.awkidev.corp.biblio.domain.persistence.BookPersistence;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.AuthorReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.BookReactive;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.AuthorEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.BookEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Repository
public class BookPersistenceMongoDb implements BookPersistence {

    private final BookReactive bookReactive;

    private final AuthorReactive authorReactive;


    @Override
    public Mono<Book> searchByIsbn(String isbn) {
        return bookReactive.findBookEntityByIsbn(isbn)
                .map(BookEntity::toBook);
    }

    @Override
    public Flux<Book> searchBooksByFilter(SearchBookFilter filter) {

        Mono<AuthorEntity> monoAuthorEntity = Mono.just(filter.getAuthorReference())
                .filter(StringUtils::isNotBlank)
                .flatMap(referenceAuthor -> authorReactive.findFirstByReference(referenceAuthor))
                .switchIfEmpty(Mono.just(new AuthorEntity()));

        return monoAuthorEntity
                .flatMapMany(authorEntity -> StringUtils.isNoneBlank(authorEntity.getId())
                        ? bookReactive.findAllByTitleContainsAndAuthorsIn(filter.getKeyword(), List.of(authorEntity))
                        : bookReactive.findAllByTitleContains(filter.getKeyword()))
                .map(BookEntity::toBook);
    }

}
