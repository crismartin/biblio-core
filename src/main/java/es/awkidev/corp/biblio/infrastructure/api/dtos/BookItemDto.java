package es.awkidev.corp.biblio.infrastructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.awkidev.corp.biblio.domain.model.Author;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Data
public class BookItemDto {
    private String isbn;
    private String title;
    private String edition;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate releaseDate;
    private String summary;
    private String authors;
    private String categories;

    public BookItemDto(Book book){
        BeanUtils.copyProperties(book, this);
        this.setAuthors(book.getAuthors().stream().map(Author::getFullName).collect(Collectors.joining(", ")));
        this.setCategories(book.getCategories().stream().map(Category::getName).collect(Collectors.joining(", ")));
    }
}
