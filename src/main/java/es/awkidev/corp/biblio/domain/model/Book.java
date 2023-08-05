package es.awkidev.corp.biblio.domain.model;

import es.awkidev.corp.biblio.infrastructure.mongodb.entities.PublisherEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private String isbn;
    private String title;
    private String edition;
    private LocalDate releaseDate;
    private String summary;
    private int numberOfCopies;
    private String signature;

    private List<Author> authors;
    private PublisherEntity publisher;
    private List<Category> categories;
}
