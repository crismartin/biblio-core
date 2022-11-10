package es.awkidev.corp.biblio.domain.model;

import java.time.LocalDate;

public class LoanBookMother {

    public static LoanBook build(){
        return LoanBook.builder()
                .reference("reference-1")
                .endDate(LocalDate.now())
                .build();
    }
}
