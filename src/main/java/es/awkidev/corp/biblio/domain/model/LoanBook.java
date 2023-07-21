package es.awkidev.corp.biblio.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanBook {

    private String reference;
    private Customer customer;
    private List<CopyBook> copyBooks;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean returned;

    private static final int NUM_LOAN_DAYS = 30;
    //TODO cambiar loan_days por mes, para que se devuelva el mismo dia
    //TODO pero del mes siguiente
    public static final int MAX_NUM_LOANS = 3;

    public String getCustomerNumberMembership(){
        return customer != null ? customer.getNumberMembership() : StringUtils.EMPTY;
    }

    public void initParameters(){
        LocalDate dateNow = LocalDate.now();

        setReference(UUID.randomUUID().toString());
        setStartDate(dateNow);
        setEndDate(dateNow.plusDays(NUM_LOAN_DAYS));
    }

}
