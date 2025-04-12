package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.domain.model.CopyBook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CopyBookService {
    Mono<CopyBook> searchByBookIsbnAvailable(String isbn);
    Mono<CopyBook> getByReference(String reference);
}
