package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.CopyBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyBookSearchDto {

    private String reference;
    private String title;
    private String isbn;

    public CopyBookSearchDto(CopyBook copyBook){
        this.reference = copyBook.getReference();
        this.title = copyBook.getBookTitle();
        this.isbn = copyBook.getBookIsbn();
    }
}
