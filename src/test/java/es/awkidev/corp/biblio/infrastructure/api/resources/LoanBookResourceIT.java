package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.infrastructure.api.RestClientTestService;
import es.awkidev.corp.biblio.infrastructure.api.dtos.LoanInfoDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.LoanNewDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static es.awkidev.corp.biblio.infrastructure.api.resources.LoanBooksResource.LOAN_BOOKS;

@RestTestConfig
class LoanBookResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private RestClientTestService restClientTestService;

    @Test
    void testCreateOk() {
        LoanNewDto loanNewDto = LoanNewDto.builder()
                .numberMembership("22222")
                .books(List.of("ref-2"))
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(LOAN_BOOKS)
                .body(Mono.just(loanNewDto), LoanNewDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanInfoDto.class)
                .value(Assertions::assertNotNull)
                .value(this::expectedLoanInfoCreated);
    }

    private void expectedLoanInfoCreated(LoanInfoDto loanInfoDto) {
        Assertions.assertNotNull(loanInfoDto.getReference());
        Assertions.assertNotNull(loanInfoDto.getEndDate());
    }
}
