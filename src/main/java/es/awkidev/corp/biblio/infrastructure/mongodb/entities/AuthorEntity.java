package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import es.awkidev.corp.biblio.domain.model.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
public class AuthorEntity {

    @Id
    @Indexed(unique = true)
    private String id;
    private String fullName;

    public Author toAuthor() {
        Author author = new Author();
        BeanUtils.copyProperties(this, author);
        return author;
    }

    public static List<Author> toAuthors(List<AuthorEntity> authorEntityCollection){
        return CollectionUtils.isEmpty(authorEntityCollection)
                ? Collections.emptyList()
                : authorEntityCollection.stream()
                    .map(AuthorEntity::toAuthor)
                    .collect(Collectors.toList());
    }
}
