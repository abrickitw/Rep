package org.avd.kamin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Ulica.
 */
@Entity
@Table(name = "ulica")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ulica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ulica_naziv", nullable = false, unique = true)
    private String ulicaNaziv;

    @ManyToOne(fetch = FetchType.LAZY)
    private Grad grad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "grad" }, allowSetters = true)
    private Naselje naselje;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ulica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUlicaNaziv() {
        return this.ulicaNaziv;
    }

    public Ulica ulicaNaziv(String ulicaNaziv) {
        this.setUlicaNaziv(ulicaNaziv);
        return this;
    }

    public void setUlicaNaziv(String ulicaNaziv) {
        this.ulicaNaziv = ulicaNaziv;
    }

    public Grad getGrad() {
        return this.grad;
    }

    public void setGrad(Grad grad) {
        this.grad = grad;
    }

    public Ulica grad(Grad grad) {
        this.setGrad(grad);
        return this;
    }

    public Naselje getNaselje() {
        return this.naselje;
    }

    public void setNaselje(Naselje naselje) {
        this.naselje = naselje;
    }

    public Ulica naselje(Naselje naselje) {
        this.setNaselje(naselje);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ulica)) {
            return false;
        }
        return getId() != null && getId().equals(((Ulica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ulica{" +
            "id=" + getId() +
            ", ulicaNaziv='" + getUlicaNaziv() + "'" +
            "}";
    }
}
