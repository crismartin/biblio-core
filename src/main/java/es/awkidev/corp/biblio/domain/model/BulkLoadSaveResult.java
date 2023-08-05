package es.awkidev.corp.biblio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkLoadSaveResult {
    private int saved;
    private List<BulkLoadError> errors = new ArrayList<>();

    public void addError(BulkLoadError error){
        errors.add(error);
    }

    public void calcSaved(int totalBooks) {
        this.saved = totalBooks - errors.size();
    }
}
