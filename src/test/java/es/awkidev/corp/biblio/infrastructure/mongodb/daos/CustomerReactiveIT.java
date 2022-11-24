package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class CustomerReactiveIT {

    @Autowired
    private CustomerReactive customerReactive;

    @Test
    void testFindCustomerEntityByNumberMembershipOk(){
        String numberMembership = "11111";

        StepVerifier
                .create(this.customerReactive.findCustomerEntityByNumberMembership(numberMembership))
                .expectNextMatches(customerEntity -> {
                    assertNotNull(customerEntity);
                    assertNotNull(customerEntity.getId());
                    assertEquals(numberMembership, customerEntity.getNumberMembership());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindCustomerEntityByNumberMembershipKO(){
        String numberMembership = "00000";

        StepVerifier
                .create(this.customerReactive.findCustomerEntityByNumberMembership(numberMembership))
                .expectNextCount(0)
                .verifyComplete();
    }

}
