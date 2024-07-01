package org.avd.kamin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Raspored.
 */
@Entity
@Table(name = "raspored")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Raspored implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "datum_usluge", nullable = false)
    private LocalDate datumUsluge;

    @ManyToOne(fetch = FetchType.LAZY)
    private Grad grad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "grad" }, allowSetters = true)
    private Naselje naselje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "grad", "naselje" }, allowSetters = true)
    private Ulica ulica;

    @ManyToOne(fetch = FetchType.LAZY)
    private User korisnikKreirao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Raspored id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatumUsluge() {
        return this.datumUsluge;
    }

    public Raspored datumUsluge(LocalDate datumUsluge) {
        this.setDatumUsluge(datumUsluge);
        return this;
    }

    public void setDatumUsluge(LocalDate datumUsluge) {
        this.datumUsluge = datumUsluge;
    }

    public Grad getGrad() {
        return this.grad;
    }

    public void setGrad(Grad grad) {
        this.grad = grad;
    }

    public Raspored grad(Grad grad) {
        this.setGrad(grad);
        return this;
    }

    public Naselje getNaselje() {
        return this.naselje;
    }

    public void setNaselje(Naselje naselje) {
        this.naselje = naselje;
    }

    public Raspored naselje(Naselje naselje) {
        this.setNaselje(naselje);
        return this;
    }

    public Ulica getUlica() {
        return this.ulica;
    }

    public void setUlica(Ulica ulica) {
        this.ulica = ulica;
    }

    public Raspored ulica(Ulica ulica) {
        this.setUlica(ulica);
        return this;
    }

    public User getKorisnikKreirao() {
        return this.korisnikKreirao;
    }

    public void setKorisnikKreirao(User user) {
        this.korisnikKreirao = user;
    }

    public Raspored korisnikKreirao(User user) {
        this.setKorisnikKreirao(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Raspored)) {
            return false;
        }
        return getId() != null && getId().equals(((Raspored) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Raspored{" +
            "id=" + getId() +
            ", datumUsluge='" + getDatumUsluge() + "'" +
            "}";
    }
}
