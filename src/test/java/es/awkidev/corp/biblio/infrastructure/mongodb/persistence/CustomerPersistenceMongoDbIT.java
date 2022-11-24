package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class CustomerPersistenceMongoDbIT {

    @Autowired
    private CustomerPersistenceMongoDb customerPersistenceMongoDb;

    @Test
    void testSearchByNumberMembershipOk(){
        String numberMembership = "11111";
        StepVerifier
                .create(this.customerPersistenceMongoDb.searchByNumberMembership(numberMembership))
                .expectNextMatches(customer -> {
                    assertNotNull(customer);
                    assertEquals(numberMembership, customer.getNumberMembership());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testSearchByNumberMembershipKO(){
        String numberMembership = "00000";
        StepVerifier
                .create(this.customerPersistenceMongoDb.searchByNumberMembership(numberMembership))
                .expectNextCount(0)
                .verifyComplete();
    }

}
