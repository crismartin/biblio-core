package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String name;

    public CategoryDto(Category category){
        BeanUtils.copyProperties(category, this);
    }

    public static List<CategoryDto> toCategoriesDto(List<Category> categories){
        return CollectionUtils.isEmpty(categories)
                ? Collections.emptyList()
                : categories.stream()
                    .map(CategoryDto::new)
                    .collect(Collectors.toList());
    }
}
