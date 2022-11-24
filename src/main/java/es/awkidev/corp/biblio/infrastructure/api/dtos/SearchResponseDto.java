package es.awkidev.corp.biblio.infrastructure.api.dtos;

import lombok.Data;

@Data
public class SearchResponseDto<T> {
    public T element;
}
