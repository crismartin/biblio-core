package es.awkidev.corp.biblio.domain.persistence;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.BulkLoadSaveResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BulkLoadPersistence {

    BulkLoadSaveResult saveBooks(List<Book> books);
}
