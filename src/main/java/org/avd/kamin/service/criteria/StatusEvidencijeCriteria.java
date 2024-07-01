package org.avd.kamin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.avd.kamin.domain.StatusEvidencije} entity. This class is used
 * in {@link org.avd.kamin.web.rest.StatusEvidencijeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /status-evidencijes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusEvidencijeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter statusEvidencijeNaziv;

    private Boolean distinct;

    public StatusEvidencijeCriteria() {}

    public StatusEvidencijeCriteria(StatusEvidencijeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.statusEvidencijeNaziv = other.optionalStatusEvidencijeNaziv().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StatusEvidencijeCriteria copy() {
        return new StatusEvidencijeCriteria(this);
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

    public StringFilter getStatusEvidencijeNaziv() {
        return statusEvidencijeNaziv;
    }

    public Optional<StringFilter> optionalStatusEvidencijeNaziv() {
        return Optional.ofNullable(statusEvidencijeNaziv);
    }

    public StringFilter statusEvidencijeNaziv() {
        if (statusEvidencijeNaziv == null) {
            setStatusEvidencijeNaziv(new StringFilter());
        }
        return statusEvidencijeNaziv;
    }

    public void setStatusEvidencijeNaziv(StringFilter statusEvidencijeNaziv) {
        this.statusEvidencijeNaziv = statusEvidencijeNaziv;
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
        final StatusEvidencijeCriteria that = (StatusEvidencijeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(statusEvidencijeNaziv, that.statusEvidencijeNaziv) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statusEvidencijeNaziv, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusEvidencijeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatusEvidencijeNaziv().map(f -> "statusEvidencijeNaziv=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
