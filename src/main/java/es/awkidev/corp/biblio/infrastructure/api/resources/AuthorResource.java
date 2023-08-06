package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.domain.model.Author;
import es.awkidev.corp.biblio.domain.services.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static es.awkidev.corp.biblio.infrastructure.api.resources.AuthorResource.AUTHORS;

@Slf4j
@RequiredArgsConstructor
@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(AUTHORS)
public class AuthorResource {

    static final String AUTHORS = "/library/authors";

    private final AuthorService authorService;

    @GetMapping
    public Mono<List<String>> searchByFullName(@RequestParam String fullName){
        var authorFullName = StringUtils.defaultString(fullName, StringUtils.EMPTY).trim().toUpperCase();
        log.info("Search authors by full name '{}'", authorFullName);

        return authorService.searchByFullName(authorFullName)
                .map(Author::getFullName)
                .doOnNext(authorFound -> log.info("  |_ Autor encontrado: {}", authorFound))
                .collectList();
    }

}
