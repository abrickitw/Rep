package org.avd.kamin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.avd.kamin.domain.Naselje} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NaseljeDTO implements Serializable {

    private Long id;

    @NotNull
    private String naseljeNaziv;

    private GradDTO grad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaseljeNaziv() {
        return naseljeNaziv;
    }

    public void setNaseljeNaziv(String naseljeNaziv) {
        this.naseljeNaziv = naseljeNaziv;
    }

    public GradDTO getGrad() {
        return grad;
    }

    public void setGrad(GradDTO grad) {
        this.grad = grad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NaseljeDTO)) {
            return false;
        }

        NaseljeDTO naseljeDTO = (NaseljeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, naseljeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NaseljeDTO{" +
            "id=" + getId() +
            ", naseljeNaziv='" + getNaseljeNaziv() + "'" +
            ", grad=" + getGrad() +
            "}";
    }
}
