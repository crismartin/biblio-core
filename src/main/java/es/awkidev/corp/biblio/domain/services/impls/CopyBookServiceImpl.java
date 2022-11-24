package es.awkidev.corp.biblio.domain.services.impls;

import es.awkidev.corp.biblio.domain.model.CopyBook;
import es.awkidev.corp.biblio.domain.persistence.CopyBookPersistence;
import es.awkidev.corp.biblio.domain.services.CopyBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CopyBookServiceImpl implements CopyBookService {

    private CopyBookPersistence copyBookPersistence;

    @Autowired
    public CopyBookServiceImpl(CopyBookPersistence copyBookPersistence){
        this.copyBookPersistence = copyBookPersistence;
    }

    @Override
    public Mono<CopyBook> searchByBookIsbnAvailable(String isbn) {
        return copyBookPersistence.searchByBookIsbnAvailable(isbn);
    }

    @Override
    public Mono<CopyBook> getByReference(String reference) {
        return copyBookPersistence.getByReference(reference);
    }
}
