package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.infrastructure.api.RestClientTestService;
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
                .numberMembership("11111")
                .books(List.of("9788457089870"))
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(LOAN_BOOKS)
                .body(Mono.just(loanNewDto), LoanNewDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .value(Assertions::assertTrue);
    }
}
