package es.awkidev.corp.biblio.domain.services.impls;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.BulkLoadSaveResult;
import es.awkidev.corp.biblio.domain.persistence.BulkLoadPersistence;
import es.awkidev.corp.biblio.domain.services.BulkLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BulkLoadServiceImpl implements BulkLoadService {

    private final BulkLoadPersistence bulkLoadPersistence;

    @Override
    public BulkLoadSaveResult saveBooks(List<Book> books){
        return bulkLoadPersistence.saveBooks(books);
    }

}
