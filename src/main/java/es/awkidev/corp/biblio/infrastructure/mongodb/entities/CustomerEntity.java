package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import es.awkidev.corp.biblio.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class CustomerEntity {

    @Id
    @Indexed(unique = true)
    private String id;
    private String name;
    private String surname;
    private String secondSurname;
    private String nick;
    @Indexed(unique = true)
    private String identity;
    @Indexed(unique = true)
    private String numberMembership;

    public Customer mapToCustomer(){
        Customer customer = new Customer();
        BeanUtils.copyProperties(this, customer);
        return customer;
    }
}