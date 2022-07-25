package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class LoanBookEntity {

    @Id
    @Indexed(unique = true)
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean returned;

    @DBRef(lazy = true)
    private BookEntity book;
    @DBRef(lazy = true)
    private CustomerEntity customer;

}
