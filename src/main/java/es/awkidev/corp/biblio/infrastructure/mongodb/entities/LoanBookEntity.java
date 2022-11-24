package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import es.awkidev.corp.biblio.domain.model.LoanBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
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
public class LoanBookEntity {

    @Id
    private String id;
    private String reference;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean returned; //FIXME Esto se deberia poder quitar, repetido de CopyBookEntity

    @DBRef
    private List<CopyBookEntity> copyBookEntities;
    @DBRef(lazy = true)
    private CustomerEntity customerEntity;

    public String getNumberMembership(){
        return customerEntity != null
                ? customerEntity.getNumberMembership()
                : null;
    }

    public LoanBook toLoanBook(){
        LoanBook loanBook = new LoanBook();
        BeanUtils.copyProperties(this, loanBook);
        loanBook.setCustomer(this.getCustomerEntity().toCustomer());
        return loanBook;
    }
}
