package es.awkidev.corp.biblio.infrastructure.api.converters.impls;

import es.awkidev.corp.biblio.domain.model.Author;
import es.awkidev.corp.biblio.domain.model.Book;
import es.awkidev.corp.biblio.domain.model.BulkLoadErrorCode;
import es.awkidev.corp.biblio.domain.model.Category;
import es.awkidev.corp.biblio.infrastructure.api.converters.BulkLoadConverter;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadBookDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadConverterResultDto;
import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadErrorDto;
import es.awkidev.corp.biblio.infrastructure.api.validations.BulkLoadBookValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class BulkLoadConverterImpl implements BulkLoadConverter {

    private final BulkLoadBookValidator bulkLoadBookValidator;

    public BulkLoadConverterResultDto convertBooks(Collection<BulkLoadBookDto> bulkLoadBooks){
        BulkLoadConverterResultDto result = new BulkLoadConverterResultDto();

        var books = bulkLoadBooks.stream()
                 .filter(bulkLoadBookDto -> isValidBulkLoadBook(bulkLoadBookDto, result))
                 .map(bulkLoadBookDto -> toBook(bulkLoadBookDto))
                 .collect(Collectors.toList());

        result.setBooks(books);

        return result;
    }

    private boolean isValidBulkLoadBook(BulkLoadBookDto bulkLoadBookDto, BulkLoadConverterResultDto result){
        StringBuilder errors = new StringBuilder();
        boolean bookValid = true;

        bulkLoadBookValidator.validate(bulkLoadBookDto, errors);

        String errorResult = errors.toString();
        if(StringUtils.isNoneBlank(errorResult)){
            result.addError(createErrorConvertion(bulkLoadBookDto, errorResult));
            log.warn("El libro {} no ha pasado el preprocesamiento", bulkLoadBookDto);
            bookValid = false;
        }
        return bookValid;
    }

    private BulkLoadErrorDto createErrorConvertion(BulkLoadBookDto bulkLoadBookDto, String errors){
        return BulkLoadErrorDto.builder()
                .bookTitle(bulkLoadBookDto.getTitle())
                .code(BulkLoadErrorCode.CONVERTION.toString())
                .message(errors)
                .build();
    }

    private Book toBook(BulkLoadBookDto bulkLoadBookDto){
        var authors = parseAuthors(bulkLoadBookDto);
        var categories = parseCategories(bulkLoadBookDto);
        var releaseDate = parseReleaseDate(bulkLoadBookDto.getReleaseDate());

        return Book.builder()
                .isbn(bulkLoadBookDto.getIsbn())
                .title(bulkLoadBookDto.getTitle())
                .signature(bulkLoadBookDto.getSignature())
                .numberOfCopies(bulkLoadBookDto.getNumberOfCopies())
                .authors(authors)
                .categories(categories)
                .releaseDate(releaseDate)
                .build();
    }

    private List<Author> parseAuthors(BulkLoadBookDto bulkLoadBookDto) {
        String authors = StringUtils.defaultString(bulkLoadBookDto.getAuthor(), StringUtils.EMPTY);
        return Stream.of(authors.split(","))
                .map(authorName -> authorName.trim().toUpperCase())
                .map(authorName -> Author.builder()
                        .fullName(authorName)
                        .build())
                .collect(Collectors.toList());
    }

    private List<Category> parseCategories(BulkLoadBookDto bulkLoadBookDto) {
        String categories = StringUtils.defaultString(bulkLoadBookDto.getCategory(), StringUtils.EMPTY);
        return Stream.of(categories.split(","))
                .map(categoryName -> categoryName.trim().toUpperCase())
                .map(categoryName -> Category.builder()
                        .name(categoryName)
                        .build())
                .collect(Collectors.toList());
    }

    private LocalDate parseReleaseDate(String strDate) {
        return Stream.of("yyyy", "dd/MM/yyyy")
                .map(formatter -> parseFecha(strDate, formatter))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(null);
    }

    private Optional<LocalDate> parseFecha(String fecha, String format) {
        try {
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern(format);
            return Optional.of(LocalDate.parse(fecha, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
