package es.awkidev.corp.biblio.infrastructure.mongodb.persistence.bulkload;

import es.awkidev.corp.biblio.domain.model.Author;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous.AuthorDao;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.AuthorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BulkLoadAuthorsPersistenceMongoDb {

    private final AuthorDao authorDao;

    public List<AuthorEntity> saveAuthors(Book book){
        List<AuthorEntity> authorEntities = null;

        var authors = book.getAuthors();
        if(!CollectionUtils.isEmpty(authors)){
            log.warn("  |_ Procedemos a guardar autores para el libro '{}'", book.getTitle());
            authorEntities = authors.stream()
                    .map(this::saveAuthor)
                    .collect(Collectors.toList());
        }else{
            log.warn("  |_ El libro '{}' no tiene autores para guardar", book.getTitle());
        }

        return authorEntities;
    }

    private AuthorEntity saveAuthor(Author author) {

        var authorEntityFounded = authorDao.findByFullName(author.getFullName());
        if(Objects.isNull(authorEntityFounded)){
            log.info("    |_ Registramos nuevo autor '{}'", author.getFullName());
            authorEntityFounded = authorDao.save(new AuthorEntity(author));
        }else{
            log.info("    |_ El autor '{}' ya está registrado", author.getFullName());
        }

        return authorEntityFounded;
    }
}
