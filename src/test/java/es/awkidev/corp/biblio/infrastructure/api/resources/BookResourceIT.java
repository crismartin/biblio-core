package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.infrastructure.api.RestClientTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.awkidev.corp.biblio.infrastructure.api.resources.BookResource.BOOKS;
import static es.awkidev.corp.biblio.infrastructure.api.resources.BookResource.ISBN;

@RestTestConfig
class BookResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private RestClientTestService restClientTestService;

    @Test
    void testSearchByReferenceOk(){
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(BOOKS + ISBN, "9788425223280")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
