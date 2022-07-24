package es.awkidev.corp.biblio.domain.services.impls;

import es.awkidev.corp.biblio.domain.model.LoanBook;
import es.awkidev.corp.biblio.domain.persistence.LoanBookPersistence;
import es.awkidev.corp.biblio.domain.services.LoanBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class LoanBookServiceImpl implements LoanBookService {

    private LoanBookPersistence loanBookPersistence;

    @Autowired
    public LoanBookServiceImpl(LoanBookPersistence loanBookPersistence){
        this.loanBookPersistence = loanBookPersistence;
    }

    @Override
    public Mono<Void> create(LoanBook loanNew) {
        return loanBookPersistence.create(loanNew);
    }
}
