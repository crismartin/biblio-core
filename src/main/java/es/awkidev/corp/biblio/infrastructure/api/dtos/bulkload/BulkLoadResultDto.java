package es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BulkLoadResultDto {
    private int readed;
    private int converted;
    private int saved;
    private int failed;
    private List<BulkLoadErrorDto> errors = new ArrayList<>();

    public void addErrors(List<BulkLoadErrorDto> errorsIncoming){
        errors.addAll(errorsIncoming);
    }

    public void calcFailed(){
        failed = readed - (converted + saved);
    }
}
