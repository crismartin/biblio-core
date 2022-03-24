package es.awkidev.corp.biblio.configuration;

import es.awkidev.corp.biblio.TestConfig;
import es.awkidev.corp.biblio.infrastructure.api.http_errors.Role;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestConfig
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void testCreateToken() {
        String token = jwtService.createToken("666666000", "adm", Role.ADMIN.name());
        assertFalse(token.isEmpty());
        LogManager.getLogger(this.getClass()).info("token:" + token);
    }
}
