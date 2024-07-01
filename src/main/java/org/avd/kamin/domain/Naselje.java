package org.avd.kamin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Naselje.
 */
@Entity
@Table(name = "naselje")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Naselje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "naselje_naziv", nullable = false, unique = true)
    private String naseljeNaziv;

    @ManyToOne(fetch = FetchType.LAZY)
    private Grad grad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Naselje id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaseljeNaziv() {
        return this.naseljeNaziv;
    }

    public Naselje naseljeNaziv(String naseljeNaziv) {
        this.setNaseljeNaziv(naseljeNaziv);
        return this;
    }

    public void setNaseljeNaziv(String naseljeNaziv) {
        this.naseljeNaziv = naseljeNaziv;
    }

    public Grad getGrad() {
        return this.grad;
    }

    public void setGrad(Grad grad) {
        this.grad = grad;
    }

    public Naselje grad(Grad grad) {
        this.setGrad(grad);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Naselje)) {
            return false;
        }
        return getId() != null && getId().equals(((Naselje) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Naselje{" +
            "id=" + getId() +
            ", naseljeNaziv='" + getNaseljeNaziv() + "'" +
            "}";
    }
}
