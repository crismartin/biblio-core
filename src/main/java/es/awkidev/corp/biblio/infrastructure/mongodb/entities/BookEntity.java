package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class BookEntity {

    @Id
    @Indexed(unique = true)
    private String id;
    @Indexed(unique = true)
    private String isbn;
    private String title;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate releaseDate;
    private String summary;
    private int numberOfCopies;

    @DBRef
    private List<AuthorEntity> authors;
    @DBRef
    private PublisherEntity publisher;
    @DBRef
    private List<CategoryEntity> categories;
}
