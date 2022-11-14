package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.CopyBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyBookDto {

    private String signature;
    private String reference;
    private boolean available;
    private String section;
    private String location;

    private BookDto book;

    public CopyBookDto(CopyBook copyBook){
        this.reference = copyBook.getReference();
        this.book = new BookDto(copyBook.getBook());
    }
}
