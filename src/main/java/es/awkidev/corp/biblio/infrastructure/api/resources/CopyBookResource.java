package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.domain.services.CopyBookService;
import es.awkidev.corp.biblio.infrastructure.api.dtos.CopyBookDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.CopyBookSearchDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.SearchResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static es.awkidev.corp.biblio.infrastructure.api.resources.CopyBookResource.COPY_BOOKS;

@Slf4j
@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(COPY_BOOKS)
public class CopyBookResource {

    static final String COPY_BOOKS = "/library/copyBooks";
    static final String BOOK_ISBN = "/book/{isbn}";
    static final String REFERENCE = "/{reference}";

    private CopyBookService copyBookService;

    @Autowired
    public CopyBookResource(CopyBookService copyBookService){
        this.copyBookService = copyBookService;
    }

    @GetMapping(BOOK_ISBN)
    public Mono<SearchResponseDto<CopyBookSearchDto>> searchByIsbnAvailable(@PathVariable String isbn) {
        SearchResponseDto<CopyBookSearchDto> result = new SearchResponseDto<>();
        log.info("Search copy of book by isbn {}", isbn);

        return copyBookService.searchByBookIsbnAvailable(isbn)
                .map(CopyBookSearchDto::new)
                .map(copyBookSearchDto -> {
                    result.setElement(copyBookSearchDto);
                    return result;
                })
                .onErrorReturn(result);
    }

    @GetMapping(REFERENCE)
    public Mono<CopyBookDto> getByReference(@PathVariable String reference){
        log.info("Search copy of book by reference {}", reference);
        return copyBookService.getByReference(reference)
                .map(CopyBookDto::new);
    }

}
