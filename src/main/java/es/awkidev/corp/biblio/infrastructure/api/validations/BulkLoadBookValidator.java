package es.awkidev.corp.biblio.infrastructure.api.validations;

import es.awkidev.corp.biblio.infrastructure.api.dtos.bulkload.BulkLoadBookDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class BulkLoadBookValidator{

    public void validate(BulkLoadBookDto bulkLoadBookDto, StringBuilder errors) {
        //isbnValidation(bulkLoadBookDto, errors);
        authorValidation(bulkLoadBookDto, errors);
        titleValidation(bulkLoadBookDto, errors);
        numOfCopiesValidation(bulkLoadBookDto, errors);
    }

    private void isbnValidation(BulkLoadBookDto bulkLoadBookDto, StringBuilder errors){
        if(StringUtils.isBlank(bulkLoadBookDto.getIsbn())){
            addError(errors, "El isbn no puede estar vacío");
        }
    }

    private void authorValidation(BulkLoadBookDto bulkLoadBookDto, StringBuilder errors){
        if(StringUtils.isBlank(bulkLoadBookDto.getAuthor())){
            addError(errors, "El author no puede estar vacío");
        }
    }

    private void titleValidation(BulkLoadBookDto bulkLoadBookDto, StringBuilder errors){
        if(StringUtils.isBlank(bulkLoadBookDto.getTitle())){
            addError(errors, "El titulo no puede estar vacío");
        }
    }

    private void numOfCopiesValidation(BulkLoadBookDto bulkLoadBookDto, StringBuilder errors){
        if(bulkLoadBookDto.getNumberOfCopies() < 0){
            addError(errors, "El numero de copias no puede ser negativo");
        }
    }

    private void addError(StringBuilder errors, String msg){
        errors.append(msg).append(".");
    }

}
