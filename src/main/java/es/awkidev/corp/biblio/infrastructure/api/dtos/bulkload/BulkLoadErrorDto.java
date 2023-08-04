package es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload;

import es.awkidev.corp.biblio.domain.model.BulkLoadError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BulkLoadErrorDto {
    private String code;
    private String message;
    private String bookTitle;

    public BulkLoadErrorDto(BulkLoadError bulkLoadError){
        BeanUtils.copyProperties(bulkLoadError, this);
    }
}
