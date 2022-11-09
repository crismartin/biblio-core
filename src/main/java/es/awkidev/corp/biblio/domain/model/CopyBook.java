package es.awkidev.corp.biblio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

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

    public String getBookIsbn(){
        return Optional.ofNullable(book)
                .map(Book::getIsbn)
                .orElse(StringUtils.EMPTY);

    }

    public String getBookTitle(){
        return Optional.ofNullable(book)
                .map(Book::getTitle)
                .orElse(StringUtils.EMPTY);
    }
}
