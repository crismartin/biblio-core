package es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload;

import lombok.Data;

import java.util.List;

@Data
public class BulkLoadDto {
    private List<BulkLoadBookDto> books;
}
