package org.avd.kamin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.avd.kamin.domain.Ulica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UlicaDTO implements Serializable {

    private Long id;

    @NotNull
    private String ulicaNaziv;

    private GradDTO grad;

    private NaseljeDTO naselje;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUlicaNaziv() {
        return ulicaNaziv;
    }

    public void setUlicaNaziv(String ulicaNaziv) {
        this.ulicaNaziv = ulicaNaziv;
    }

    public GradDTO getGrad() {
        return grad;
    }

    public void setGrad(GradDTO grad) {
        this.grad = grad;
    }

    public NaseljeDTO getNaselje() {
        return naselje;
    }

    public void setNaselje(NaseljeDTO naselje) {
        this.naselje = naselje;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UlicaDTO)) {
            return false;
        }

        UlicaDTO ulicaDTO = (UlicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ulicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UlicaDTO{" +
            "id=" + getId() +
            ", ulicaNaziv='" + getUlicaNaziv() + "'" +
            ", grad=" + getGrad() +
            ", naselje=" + getNaselje() +
            "}";
    }
}
