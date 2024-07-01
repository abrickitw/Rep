package org.avd.kamin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A StatusEvidencije.
 */
@Entity
@Table(name = "status_evidencije")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusEvidencije implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status_evidencije_naziv", nullable = false)
    private String statusEvidencijeNaziv;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StatusEvidencije id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusEvidencijeNaziv() {
        return this.statusEvidencijeNaziv;
    }

    public StatusEvidencije statusEvidencijeNaziv(String statusEvidencijeNaziv) {
        this.setStatusEvidencijeNaziv(statusEvidencijeNaziv);
        return this;
    }

    public void setStatusEvidencijeNaziv(String statusEvidencijeNaziv) {
        this.statusEvidencijeNaziv = statusEvidencijeNaziv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusEvidencije)) {
            return false;
        }
        return getId() != null && getId().equals(((StatusEvidencije) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusEvidencije{" +
            "id=" + getId() +
            ", statusEvidencijeNaziv='" + getStatusEvidencijeNaziv() + "'" +
            "}";
    }
}
