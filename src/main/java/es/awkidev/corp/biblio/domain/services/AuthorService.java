package es.awkidev.corp.biblio.domain.services;

import es.awkidev.corp.biblio.domain.model.Author;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface AuthorService {
    Flux<Author> searchByFullName(String fullName);
}
