package es.awkidev.corp.biblio.infrastructure.api.converters;

import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadBookDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadConverterResultDto;

import java.util.Collection;

public interface BulkLoadConverter {

    BulkLoadConverterResultDto convertBooks(Collection<BulkLoadBookDto> bulkLoadBooks);
}
