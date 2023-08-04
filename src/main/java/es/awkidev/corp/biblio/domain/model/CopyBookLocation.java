package es.awkidev.corp.biblio.domain.model;

public enum CopyBookLocation {
    DEPOSITO,
    ESTANTERIA;

    @Override
    public String toString() {
        return this.name();
    }
}
