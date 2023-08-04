package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import es.awkidev.corp.biblio.domain.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class BookEntity {

    @Id
    @Indexed(unique = true)
    private String id;
    @Indexed(unique = true)
    private String isbn;
    private String title;
    private LocalDate releaseDate;
    private String summary;

    @DBRef
    private List<AuthorEntity> authors;
    @DBRef(lazy = true)
    private PublisherEntity publisher;
    @DBRef(lazy = true)
    private List<CategoryEntity> categories;

    public Book toBook(){
        Book book = new Book();
        BeanUtils.copyProperties(this, book);
        book.setAuthors(AuthorEntity.toAuthors(authors));
        return book;
    }

    public BookEntity(Book book){
        BeanUtils.copyProperties(book, this);
    }
}
