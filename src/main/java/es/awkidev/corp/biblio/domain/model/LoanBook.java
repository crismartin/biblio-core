package es.awkidev.corp.biblio.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.awkidev.corp.biblio.domain.model.validations.ListNotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanBook {

    private Customer customer;
    private List<Book> books;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean returned;

    private static final int NUM_LOAN_DAYS = 15;
    public static final int MAX_NUM_LOANS = 5;

    public String getNumberMembership(){
        return customer != null ? customer.getNumberMembership() : StringUtils.EMPTY;
    }

    public void addBook(Book book){
        if(books == null){
            books = new ArrayList<>();
        }
        books.add(book);
    }

    public void initDates(){
        LocalDate dateNow = LocalDate.now();
        setStartDate(dateNow);
        setEndDate(dateNow.plusDays(NUM_LOAN_DAYS));
    }

}
