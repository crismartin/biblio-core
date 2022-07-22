package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class CustomerServiceIT {

    static final String NUMBER_MEMBERSHIP_OK = "11111";
    static final String FULL_NAME = "Pepe Perez Sanchez";

    @Autowired
    private CustomerService customerService;

    @Test
    void testSearchByNumberMembershipOk(){
        StepVerifier
                .create(this.customerService.searchByNumberMembership(NUMBER_MEMBERSHIP_OK))
                .expectNextMatches(customerFullName -> {
                    assertNotNull(customerFullName, FULL_NAME);
                    return true;
                })
                .thenCancel()
                .verify();
    }
}
