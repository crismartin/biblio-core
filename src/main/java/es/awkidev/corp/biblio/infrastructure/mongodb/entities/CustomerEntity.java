package es.awkidev.corp.biblio.infrastructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
