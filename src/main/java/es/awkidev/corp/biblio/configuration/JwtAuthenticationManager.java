package es.awkidev.corp.biblio.configuration;

import es.awkidev.corp.biblio.infrastructure.api.http_errors.Role;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private JwtService jwtService;

    public JwtAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono< Authentication > authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        GrantedAuthority authority = new SimpleGrantedAuthority(Role.PREFIX + jwtService.role(token));
        return Mono.just(new UsernamePasswordAuthenticationToken(
                jwtService.user(token), authentication.getCredentials(), List.of(authority)));
    }
}
