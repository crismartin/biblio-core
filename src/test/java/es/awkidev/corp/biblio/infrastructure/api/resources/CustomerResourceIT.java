package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.infrastructure.api.RestClientTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static es.awkidev.corp.biblio.infrastructure.api.resources.CustomerResource.CUSTOMERS;
import static es.awkidev.corp.biblio.infrastructure.api.resources.CustomerResource.NUMBER_MEMBERSHIP;

@RestTestConfig
class CustomerResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private RestClientTestService restClientTestService;

    @Test
    void testSearchByReferenceOk(){
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(CUSTOMERS + NUMBER_MEMBERSHIP, "11111")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
