package org.avd.kamin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Grad.
 */
@Entity
@Table(name = "grad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Grad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "grad_naziv", nullable = false, unique = true)
    private String gradNaziv;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Grad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGradNaziv() {
        return this.gradNaziv;
    }

    public Grad gradNaziv(String gradNaziv) {
        this.setGradNaziv(gradNaziv);
        return this;
    }

    public void setGradNaziv(String gradNaziv) {
        this.gradNaziv = gradNaziv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Grad)) {
            return false;
        }
        return getId() != null && getId().equals(((Grad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Grad{" +
            "id=" + getId() +
            ", gradNaziv='" + getGradNaziv() + "'" +
            "}";
    }
}
