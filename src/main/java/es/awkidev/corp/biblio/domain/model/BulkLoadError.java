package es.awkidev.corp.biblio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BulkLoadError {
    private String code;
    private String message;
    private String bookTitle;
}
