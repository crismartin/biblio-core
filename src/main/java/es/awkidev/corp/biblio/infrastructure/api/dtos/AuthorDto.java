package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.Author;
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
public class AuthorDto {

    private String fullName;

    public AuthorDto(Author author) {
        BeanUtils.copyProperties(author, this);
    }

    public static List<AuthorDto> toAuthorsDto(List<Author> authors) {
        return CollectionUtils.isEmpty(authors)
                ? Collections.emptyList()
                : authors.stream()
                    .map(AuthorDto::new)
                    .collect(Collectors.toList());
    }
}
