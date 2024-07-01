package org.avd.kamin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A VrstaUsluge.
 */
@Entity
@Table(name = "vrsta_usluge")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VrstaUsluge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vrsta_usluge_naziv", nullable = false, unique = true)
    private String vrstaUslugeNaziv;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VrstaUsluge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVrstaUslugeNaziv() {
        return this.vrstaUslugeNaziv;
    }

    public VrstaUsluge vrstaUslugeNaziv(String vrstaUslugeNaziv) {
        this.setVrstaUslugeNaziv(vrstaUslugeNaziv);
        return this;
    }

    public void setVrstaUslugeNaziv(String vrstaUslugeNaziv) {
        this.vrstaUslugeNaziv = vrstaUslugeNaziv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VrstaUsluge)) {
            return false;
        }
        return getId() != null && getId().equals(((VrstaUsluge) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VrstaUsluge{" +
            "id=" + getId() +
            ", vrstaUslugeNaziv='" + getVrstaUslugeNaziv() + "'" +
            "}";
    }
}
