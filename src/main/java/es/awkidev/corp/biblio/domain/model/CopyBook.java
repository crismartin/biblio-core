package es.awkidev.corp.biblio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CopyBook {

    private String signature;
    private String reference;
    private boolean available;
    private String section;
    private String location;

    private Book book;
}
