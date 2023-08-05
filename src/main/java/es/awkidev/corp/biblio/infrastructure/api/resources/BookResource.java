package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.domain.services.BookService;
import es.awkidev.corp.biblio.infrastructure.api.dtos.SearchBookDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.SearchBookFilterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    static final String SEARCH = "/search";

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

    @PostMapping(SEARCH)
    public Flux<SearchBookDto> searchBooksByFilter(@RequestBody SearchBookFilterDto filter){
        log.info("Search books by filter criteria -> author: '{}', keyword: '{}'",
                filter.getAuthorReference(), filter.getKeyword());

        return bookService.searchBooksByFilter(filter.toSearchBookFilter())
                .map(SearchBookDto::new)
                .doOnNext(searchBookDto -> log.info("  |_ Libro encontrado: {}", searchBookDto.getIsbn()))
                .doOnComplete(() -> log.info("Search book by filter criteria finished\n"));
    }

}
