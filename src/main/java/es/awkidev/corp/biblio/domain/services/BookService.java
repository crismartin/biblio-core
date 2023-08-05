package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.SearchBookFilter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface BookService {
    Mono<Book> searchByIsbn(String isbn);

    Flux<Book> searchBooksByFilter(SearchBookFilter filter);
}
