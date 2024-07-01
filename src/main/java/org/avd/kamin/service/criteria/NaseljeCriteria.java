package org.avd.kamin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.avd.kamin.domain.Naselje} entity. This class is used
 * in {@link org.avd.kamin.web.rest.NaseljeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /naseljes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NaseljeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter naseljeNaziv;

    private LongFilter gradId;

    private Boolean distinct;

    public NaseljeCriteria() {}

    public NaseljeCriteria(NaseljeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.naseljeNaziv = other.optionalNaseljeNaziv().map(StringFilter::copy).orElse(null);
        this.gradId = other.optionalGradId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NaseljeCriteria copy() {
        return new NaseljeCriteria(this);
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

    public StringFilter getNaseljeNaziv() {
        return naseljeNaziv;
    }

    public Optional<StringFilter> optionalNaseljeNaziv() {
        return Optional.ofNullable(naseljeNaziv);
    }

    public StringFilter naseljeNaziv() {
        if (naseljeNaziv == null) {
            setNaseljeNaziv(new StringFilter());
        }
        return naseljeNaziv;
    }

    public void setNaseljeNaziv(StringFilter naseljeNaziv) {
        this.naseljeNaziv = naseljeNaziv;
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
        final NaseljeCriteria that = (NaseljeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(naseljeNaziv, that.naseljeNaziv) &&
            Objects.equals(gradId, that.gradId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, naseljeNaziv, gradId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NaseljeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNaseljeNaziv().map(f -> "naseljeNaziv=" + f + ", ").orElse("") +
            optionalGradId().map(f -> "gradId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
