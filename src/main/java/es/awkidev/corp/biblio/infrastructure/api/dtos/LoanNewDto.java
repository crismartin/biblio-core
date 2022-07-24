package es.awkidev.corp.biblio.infrastructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanNewDto {

    private String numberMembership;
    private List<String> idsBooks;
}
