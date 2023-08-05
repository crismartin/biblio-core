package es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload;

import lombok.Data;

@Data
public class BulkLoadBookDto {

    private String title;
    private String author;
    private String category;
    private String releaseDate;
    private int numberOfCopies;
    private String signature;
    private String isbn;
}
