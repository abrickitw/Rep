package org.avd.kamin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.avd.kamin.domain.Raspored} entity. This class is used
 * in {@link org.avd.kamin.web.rest.RasporedResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rasporeds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RasporedCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter datumUsluge;

    private LongFilter gradId;

    private LongFilter naseljeId;

    private LongFilter ulicaId;

    private LongFilter korisnikKreiraoId;

    private Boolean distinct;

    public RasporedCriteria() {}

    public RasporedCriteria(RasporedCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.datumUsluge = other.optionalDatumUsluge().map(LocalDateFilter::copy).orElse(null);
        this.gradId = other.optionalGradId().map(LongFilter::copy).orElse(null);
        this.naseljeId = other.optionalNaseljeId().map(LongFilter::copy).orElse(null);
        this.ulicaId = other.optionalUlicaId().map(LongFilter::copy).orElse(null);
        this.korisnikKreiraoId = other.optionalKorisnikKreiraoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RasporedCriteria copy() {
        return new RasporedCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDatumUsluge() {
        return datumUsluge;
    }

    public Optional<LocalDateFilter> optionalDatumUsluge() {
        return Optional.ofNullable(datumUsluge);
    }

    public LocalDateFilter datumUsluge() {
        if (datumUsluge == null) {
            setDatumUsluge(new LocalDateFilter());
        }
        return datumUsluge;
    }

    public void setDatumUsluge(LocalDateFilter datumUsluge) {
        this.datumUsluge = datumUsluge;
    }

    public LongFilter getGradId() {
        return gradId;
    }

    public Optional<LongFilter> optionalGradId() {
        return Optional.ofNullable(gradId);
    }

    public LongFilter gradId() {
        if (gradId == null) {
            setGradId(new LongFilter());
        }
        return gradId;
    }

    public void setGradId(LongFilter gradId) {
        this.gradId = gradId;
    }

    public LongFilter getNaseljeId() {
        return naseljeId;
    }

    public Optional<LongFilter> optionalNaseljeId() {
        return Optional.ofNullable(naseljeId);
    }

    public LongFilter naseljeId() {
        if (naseljeId == null) {
            setNaseljeId(new LongFilter());
        }
        return naseljeId;
    }

    public void setNaseljeId(LongFilter naseljeId) {
        this.naseljeId = naseljeId;
    }

    public LongFilter getUlicaId() {
        return ulicaId;
    }

    public Optional<LongFilter> optionalUlicaId() {
        return Optional.ofNullable(ulicaId);
    }

    public LongFilter ulicaId() {
        if (ulicaId == null) {
            setUlicaId(new LongFilter());
        }
        return ulicaId;
    }

    public void setUlicaId(LongFilter ulicaId) {
        this.ulicaId = ulicaId;
    }

    public LongFilter getKorisnikKreiraoId() {
        return korisnikKreiraoId;
    }

    public Optional<LongFilter> optionalKorisnikKreiraoId() {
        return Optional.ofNullable(korisnikKreiraoId);
    }

    public LongFilter korisnikKreiraoId() {
        if (korisnikKreiraoId == null) {
            setKorisnikKreiraoId(new LongFilter());
        }
        return korisnikKreiraoId;
    }

    public void setKorisnikKreiraoId(LongFilter korisnikKreiraoId) {
        this.korisnikKreiraoId = korisnikKreiraoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RasporedCriteria that = (RasporedCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(datumUsluge, that.datumUsluge) &&
            Objects.equals(gradId, that.gradId) &&
            Objects.equals(naseljeId, that.naseljeId) &&
            Objects.equals(ulicaId, that.ulicaId) &&
            Objects.equals(korisnikKreiraoId, that.korisnikKreiraoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datumUsluge, gradId, naseljeId, ulicaId, korisnikKreiraoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RasporedCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDatumUsluge().map(f -> "datumUsluge=" + f + ", ").orElse("") +
            optionalGradId().map(f -> "gradId=" + f + ", ").orElse("") +
            optionalNaseljeId().map(f -> "naseljeId=" + f + ", ").orElse("") +
            optionalUlicaId().map(f -> "ulicaId=" + f + ", ").orElse("") +
            optionalKorisnikKreiraoId().map(f -> "korisnikKreiraoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
