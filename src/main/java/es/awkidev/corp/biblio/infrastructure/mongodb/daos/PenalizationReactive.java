package es.awkidev.corp.biblio.infrastructure.mongodb.daos;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CustomerEntity;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.PenalizationEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface PenalizationReactive extends ReactiveSortingRepository<PenalizationEntity, String> {

    Mono<PenalizationEntity> findFirstByCustomerEntityAndActiveTrue(CustomerEntity customerEntity);

    Mono<PenalizationEntity> findFirstByCustomerEntity_NumberMembershipAndActiveTrue(String numberMembership);

    Mono<Boolean> existsPenalizationEntityByCustomerEntityAndActiveTrue(CustomerEntity customerEntity);
}
