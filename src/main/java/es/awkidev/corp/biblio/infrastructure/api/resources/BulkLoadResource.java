package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.domain.model.BulkLoadSaveResult;
import es.awkidev.corp.biblio.domain.services.BulkLoadService;
import es.awkidev.corp.biblio.infrastructure.api.converters.BulkLoadConverter;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadBookDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadConverterResultDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadErrorDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static es.awkidev.corp.biblio.infrastructure.api.resources.BulkLoadResource.BULK_LOAD;

@Slf4j
@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(BULK_LOAD)
@RequiredArgsConstructor
public class BulkLoadResource {
    static final String BULK_LOAD = "/bulk-load";

    private final BulkLoadConverter converterService;

    private final BulkLoadService bulkLoadService;

    @PostMapping
    public BulkLoadResultDto addBooks(@RequestBody List<BulkLoadBookDto> bulkLoadBooks) {
        BulkLoadResultDto result = new BulkLoadResultDto();

        // Readed
        result.setReaded(bulkLoadBooks.size());

        // Convertion
        BulkLoadConverterResultDto convertionResult = converterService.convertBooks(bulkLoadBooks);
        result.setConverted(convertionResult.getTotalConverted());
        result.addErrors(convertionResult.getErrors());

        // Saved
        BulkLoadSaveResult saveResult = bulkLoadService.saveBooks(convertionResult.getBooks());
        result.setSaved(saveResult.getSaved());
        result.addErrors(saveResult.getErrors().stream()
                .map(BulkLoadErrorDto::new)
                .collect(Collectors.toList())
        );

        // final
        result.calcFailed();

        return result;
    }

}
