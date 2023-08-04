package es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryDao extends MongoRepository<CategoryEntity, String> {

    CategoryEntity findByName(String name);

}
