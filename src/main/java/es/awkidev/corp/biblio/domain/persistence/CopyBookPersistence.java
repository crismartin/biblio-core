package es.awkidev.corp.biblio.domain.persistence;

import es.awkidev.corp.biblio.domain.model.CopyBook;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CopyBookPersistence {
    Mono<CopyBook> searchByBookIsbnAvailable(String isbn);

    Mono<CopyBook> getByReference(String reference);
}
