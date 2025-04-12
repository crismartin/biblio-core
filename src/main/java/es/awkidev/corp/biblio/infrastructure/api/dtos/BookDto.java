package es.awkidev.corp.biblio.infrastructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate releaseDate;
    private String summary;
    private int numberOfCopies;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate nearAvailabilityDate;

    private List<AuthorDto> authors;
    private List<CategoryDto> categories;

    public BookDto(Book book){
        BeanUtils.copyProperties(book, this);
        this.setAuthors(AuthorDto.toAuthorsDto(book.getAuthors()));
        this.setCategories(CategoryDto.toCategoriesDto(book.getCategories()));
    }
}
