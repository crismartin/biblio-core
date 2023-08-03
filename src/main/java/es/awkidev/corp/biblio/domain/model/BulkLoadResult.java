package es.awkidev.corp.biblio.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class BulkLoadResult {
    private int readed;
    private int converted;
    private int saved;
    private int failed;
    private List<Book> books;
}
