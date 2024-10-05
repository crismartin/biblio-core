package es.awkidev.corp.biblio.infrastructure.mongodb.persistence;

import es.awkidev.corp.biblio.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class AuthorPersistenceMongoDbIT {

    @Autowired
    AuthorPersistenceMongoDb authorPersistence;

    @Test
    void testSearchByFullName(){

        String keywordName = "amador";

        StepVerifier
                .create(this.authorPersistence.searchByFullName(keywordName))
                .expectNextMatches(author -> {
                    assertNotNull(author);
                    return true;
                })
                .expectComplete()
                .verify();

    }
}
