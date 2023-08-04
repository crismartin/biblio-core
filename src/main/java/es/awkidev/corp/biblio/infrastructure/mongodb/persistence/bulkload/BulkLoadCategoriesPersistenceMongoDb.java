package es.awkidev.corp.biblio.infrastructure.mongodb.persistence.bulkload;

import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.Category;
import es.awkidev.corp.biblio.infrastructure.mongodb.daos.synchronous.CategoryDao;
import es.awkidev.corp.biblio.infrastructure.mongodb.entities.CategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BulkLoadCategoriesPersistenceMongoDb {

    private final CategoryDao categoryDao;

    public List<CategoryEntity> saveCategories(Book book){
        List<CategoryEntity> categoryEntities = null;

        var categories = book.getCategories();
        if(!CollectionUtils.isEmpty(categories)){
            log.warn("  |_ Procedemos a guardar categorias para el libro '{}'", book.getTitle());
            categoryEntities = categories.stream()
                    .map(this::saveCategory)
                    .collect(Collectors.toList());
        }else{
            log.warn("  |_ El libro '{}' no tiene categorias para guardar", book.getTitle());
        }

        return categoryEntities;
    }

    private CategoryEntity saveCategory(Category category) {

        var authorEntityFounded = categoryDao.findByName(category.getName());
        if(Objects.isNull(authorEntityFounded)){
            log.info("    |_ Registramos nueva categoria '{}'", category.getName());
            authorEntityFounded = categoryDao.save(new CategoryEntity(category));
        }else{
            log.info("    |_ La categoria '{}' ya está registrado", category.getName());
        }

        return authorEntityFounded;
    }

}
