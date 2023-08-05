package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import es.awkidev.corp.biblio.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class CategoryEntity {

    @Id
    private String id;
    private String name;

    public CategoryEntity(Category category){
        BeanUtils.copyProperties(category, this);
    }

    public Category toCategory(){
        Category category = new Category();
        BeanUtils.copyProperties(this, category);
        return category;
    }

    public static List<Category> toCategories(List<CategoryEntity> categoryEntities) {
        return CollectionUtils.isEmpty(categoryEntities)
                ? Collections.emptyList()
                : categoryEntities.stream()
                .map(CategoryEntity::toCategory)
                .collect(Collectors.toList());
    }

}
