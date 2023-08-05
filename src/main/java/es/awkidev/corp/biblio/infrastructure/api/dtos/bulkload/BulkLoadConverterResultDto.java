package es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload;

import es.awkidev.corp.biblio.domain.model.Book;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BulkLoadConverterResultDto {
    private List<BulkLoadErrorDto> errors = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    public  void addError(BulkLoadErrorDto error){
        errors.add(error);
    }

    public int getTotalConverted(){
        return books.size();
    }
}
