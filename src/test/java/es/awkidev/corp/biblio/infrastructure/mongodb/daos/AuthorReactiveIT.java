package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class AuthorReactiveIT {

    @Autowired
    AuthorReactive authorPersistence;

    @Test
    void testFindFirstByFullName(){
        String keywordName = "AMADOR RIVAS";

        StepVerifier
                .create(this.authorPersistence.findFirstByFullName(keywordName))
                .expectNextMatches(author -> {
                    assertNotNull(author);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testFindAllByFullNameContains(){
        String keywordName = "AMADOR";

        StepVerifier
                .create(this.authorPersistence.findAllByFullNameContains(keywordName))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .expectRecordedMatches(elements ->  !elements.isEmpty() && elements.size() == 1)
                .verifyComplete();
    }
}
