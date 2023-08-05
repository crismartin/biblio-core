package es.awkidev.corp.biblio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchBookFilter {
    private String title;
    private String authorReference;
    private String keyword;
}
