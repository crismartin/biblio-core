package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.BulkLoadSaveResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BulkLoadService {
    BulkLoadSaveResult saveBooks(List<Book> books);
}
