package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.domain.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static es.awkidev.corp.biblio.infrastructure.api.resources.BookResource.BOOKS;

@Slf4j
@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(BOOKS)
public class BookResource {
    static final String BOOKS = "/library/books";
    static final String ISBN = "/{isbn}";

    private BookService bookService;

    @Autowired
    public BookResource(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping(ISBN)
    public Mono<List<String>> searchByIsbnAvailable(@PathVariable String isbn) {
        log.info("Search book by isbn {}", isbn);
        return bookService.searchByIsbn(isbn)
                .map(book -> book.getNumberOfCopies() > 0
                        ? List.of(book.getIsbn() + " - " + book.getTitle())
                        : List.of("Libro no disponible"));
    }
}
