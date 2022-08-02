package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    private String id;
    private String reference;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean returned; //FIXME Esto se deberia poder quitar, repetido de CopyBookEntity

    @DBRef
    private CopyBookEntity copyBookEntity;
    @DBRef(lazy = true)
    private CustomerEntity customerEntity;

    public String getNumberMembership(){
        return customerEntity != null
                ? customerEntity.getNumberMembership()
                : null;
    }
}
