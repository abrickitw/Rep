package org.avd.kamin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.avd.kamin.domain.VrstaUsluge} entity. This class is used
 * in {@link org.avd.kamin.web.rest.VrstaUslugeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vrsta-usluges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VrstaUslugeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter vrstaUslugeNaziv;

    private Boolean distinct;

    public VrstaUslugeCriteria() {}

    public VrstaUslugeCriteria(VrstaUslugeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.vrstaUslugeNaziv = other.optionalVrstaUslugeNaziv().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VrstaUslugeCriteria copy() {
        return new VrstaUslugeCriteria(this);
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

    public StringFilter getVrstaUslugeNaziv() {
        return vrstaUslugeNaziv;
    }

    public Optional<StringFilter> optionalVrstaUslugeNaziv() {
        return Optional.ofNullable(vrstaUslugeNaziv);
    }

    public StringFilter vrstaUslugeNaziv() {
        if (vrstaUslugeNaziv == null) {
            setVrstaUslugeNaziv(new StringFilter());
        }
        return vrstaUslugeNaziv;
    }

    public void setVrstaUslugeNaziv(StringFilter vrstaUslugeNaziv) {
        this.vrstaUslugeNaziv = vrstaUslugeNaziv;
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
        final VrstaUslugeCriteria that = (VrstaUslugeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(vrstaUslugeNaziv, that.vrstaUslugeNaziv) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vrstaUslugeNaziv, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VrstaUslugeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVrstaUslugeNaziv().map(f -> "vrstaUslugeNaziv=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
