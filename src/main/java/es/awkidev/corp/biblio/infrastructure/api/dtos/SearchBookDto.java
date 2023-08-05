package es.awkidev.corp.biblio.infrastructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.awkidev.corp.biblio.domain.model.Book;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;

@Data
public class SearchBookDto {
    private String isbn;
    private String title;
    private String edition;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate releaseDate;
    private String summary;

    private List<AuthorDto> authors;
    private List<CategoryDto> categories;

    public SearchBookDto(Book book){
        BeanUtils.copyProperties(book, this);
        this.setAuthors(AuthorDto.toAuthorsDto(book.getAuthors()));
        this.setCategories(CategoryDto.toCategoriesDto(book.getCategories()));
    }
}
