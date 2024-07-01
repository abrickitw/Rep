package org.avd.kamin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.avd.kamin.domain.Grad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GradDTO implements Serializable {

    private Long id;

    @NotNull
    private String gradNaziv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGradNaziv() {
        return gradNaziv;
    }

    public void setGradNaziv(String gradNaziv) {
        this.gradNaziv = gradNaziv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradDTO)) {
            return false;
        }

        GradDTO gradDTO = (GradDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gradDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradDTO{" +
            "id=" + getId() +
            ", gradNaziv='" + getGradNaziv() + "'" +
            "}";
    }
}
