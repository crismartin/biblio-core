package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.TestConfig;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class LoanBookReactiveIT {

    @Autowired
    private LoanBookReactive loanBookReactive;

    @Test
    void testFindAllByCustomer(){
        CustomerEntity customerEntity = CustomerEntity.builder().id("3").build();

        StepVerifier
                .create(loanBookReactive.findAllByCustomerEntityAndReturnedFalse(customerEntity))
                .expectNextMatches(loanBookEntity -> {
                    assertNotNull(loanBookEntity);
                    assertEquals(customerEntity.getId(), loanBookEntity.getCustomerEntity().getId());
                    return true;
                })
                .thenCancel()
                .verify();
    }

}
