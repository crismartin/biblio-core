package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.domain.services.LoanBookService;
import es.awkidev.corp.biblio.infrastructure.api.dtos.LoanNewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(LoanBooksResource.LOAN_BOOKS)
public class LoanBooksResource {

    public static final String LOAN_BOOKS = "/library/loans";

    private LoanBookService loanBookService;

    @Autowired
    public LoanBooksResource(LoanBookService loanBookService){
        this.loanBookService = loanBookService;
    }

    @PostMapping(produces = {"application/json"})
    public Mono<Boolean> createLoan(@RequestBody LoanNewDto loanNewDto) {
        log.info("New loan incoming: {}", loanNewDto);
        return loanBookService.create(loanNewDto.toLoanBook());
    }
}
