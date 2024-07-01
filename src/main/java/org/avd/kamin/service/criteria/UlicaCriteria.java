package org.avd.kamin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.avd.kamin.domain.Ulica} entity. This class is used
 * in {@link org.avd.kamin.web.rest.UlicaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ulicas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UlicaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ulicaNaziv;

    private LongFilter gradId;

    private LongFilter naseljeId;

    private Boolean distinct;

    public UlicaCriteria() {}

    public UlicaCriteria(UlicaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.ulicaNaziv = other.optionalUlicaNaziv().map(StringFilter::copy).orElse(null);
        this.gradId = other.optionalGradId().map(LongFilter::copy).orElse(null);
        this.naseljeId = other.optionalNaseljeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UlicaCriteria copy() {
        return new UlicaCriteria(this);
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

    public StringFilter getUlicaNaziv() {
        return ulicaNaziv;
    }

    public Optional<StringFilter> optionalUlicaNaziv() {
        return Optional.ofNullable(ulicaNaziv);
    }

    public StringFilter ulicaNaziv() {
        if (ulicaNaziv == null) {
            setUlicaNaziv(new StringFilter());
        }
        return ulicaNaziv;
    }

    public void setUlicaNaziv(StringFilter ulicaNaziv) {
        this.ulicaNaziv = ulicaNaziv;
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
        final UlicaCriteria that = (UlicaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ulicaNaziv, that.ulicaNaziv) &&
            Objects.equals(gradId, that.gradId) &&
            Objects.equals(naseljeId, that.naseljeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ulicaNaziv, gradId, naseljeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UlicaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUlicaNaziv().map(f -> "ulicaNaziv=" + f + ", ").orElse("") +
            optionalGradId().map(f -> "gradId=" + f + ", ").orElse("") +
            optionalNaseljeId().map(f -> "naseljeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
