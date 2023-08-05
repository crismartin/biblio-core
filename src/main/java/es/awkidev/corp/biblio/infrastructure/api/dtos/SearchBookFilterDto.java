package es.awkidev.corp.biblio.infrastructure.api.dtos;

import es.awkidev.corp.biblio.domain.model.SearchBookFilter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SearchBookFilterDto {
    private String authorReference;
    private String keyword;

    public SearchBookFilter toSearchBookFilter(){
        return SearchBookFilter.builder()
                .keyword(StringUtils.defaultString(this.keyword, StringUtils.EMPTY).toUpperCase())
                .authorReference(StringUtils.defaultString(this.authorReference, StringUtils.EMPTY))
                .build();
    }
}
