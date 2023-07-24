package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class PenalizationEntity {

    @Id
    private String id;
    private String reference;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    @DBRef
    private CustomerEntity customerEntity;
    @DBRef
    private List<LoanBookEntity> loanBookEntities;
}
