package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.infrastructure.api.dtos.LoanNewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(LoanBooksResource.LOAN_BOOKS)
public class LoanBooksResource {

    public static final String LOAN_BOOKS = "/library/loans";

    @Autowired
    public LoanBooksResource(){
    }

    @PostMapping(produces = {"application/json"})
    public Mono<Boolean> createLoan(LoanNewDto loanNewDto) {
        log.info("New loan incoming: {}", loanNewDto);
        return Mono.just(true);
    }
}
