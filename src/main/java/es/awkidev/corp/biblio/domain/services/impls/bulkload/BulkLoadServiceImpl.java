package es.awkidev.corp.biblio.domain.services.impls.bulkload;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.BulkLoadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BulkLoadServiceImpl {

    public BulkLoadResult addBooks(List<Book> books){
        return null;
    }

}
