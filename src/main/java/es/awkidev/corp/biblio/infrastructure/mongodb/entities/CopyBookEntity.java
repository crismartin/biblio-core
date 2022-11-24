package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import es.awkidev.corp.biblio.domain.model.CopyBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class CopyBookEntity {

    @Id
    private String id;
    @Indexed(unique = true)
    private String reference;
    private String signature;
    private boolean available;
    private String section;
    private String location;

    @DBRef(lazy = true)
    private BookEntity bookEntity;

    public CopyBook toCopyBook() {
        CopyBook copyBook = new CopyBook();
        BeanUtils.copyProperties(this, copyBook);
        return copyBook;
    }
}
