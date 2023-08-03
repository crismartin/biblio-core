package es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload;

import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadBookDto;
import lombok.Data;

import java.util.List;

@Data
public class BulkLoadDto {
    private List<BulkLoadBookDto> books;
}
