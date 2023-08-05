package es.awkidev.corp.biblio.domain.model;

public enum BulkLoadErrorCode {
    CONVERTION,
    CREATION;

    @Override
    public String toString(){
        return this.name();
    }
}
