package es.awkidev.corp.biblio.domain.services.impls;

import es.awkidev.corp.biblio.domain.model.Author;
import es.awkidev.corp.biblio.domain.persistence.AuthorPersistence;
import es.awkidev.corp.biblio.domain.services.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorPersistence authorPersistence;

    @Override
    public Flux<Author> searchByFullName(String fullName) {
        return authorPersistence.searchByFullName(fullName);
    }
}
