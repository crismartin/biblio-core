package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String isbn;
    private String title;
    private LocalDate releaseDate;
    private String summary;
    private int numberOfCopies;

    private List<AuthorDto> authors;

    public BookDto(Book book){
        BeanUtils.copyProperties(book, this);
        this.authors = AuthorDto.toAuthorsDto(book.getAuthors());
    }
}
