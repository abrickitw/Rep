package org.avd.kamin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.avd.kamin.domain.VrstaUsluge} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VrstaUslugeDTO implements Serializable {

    private Long id;

    @NotNull
    private String vrstaUslugeNaziv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVrstaUslugeNaziv() {
        return vrstaUslugeNaziv;
    }

    public void setVrstaUslugeNaziv(String vrstaUslugeNaziv) {
        this.vrstaUslugeNaziv = vrstaUslugeNaziv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VrstaUslugeDTO)) {
            return false;
        }

        VrstaUslugeDTO vrstaUslugeDTO = (VrstaUslugeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vrstaUslugeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VrstaUslugeDTO{" +
            "id=" + getId() +
            ", vrstaUslugeNaziv='" + getVrstaUslugeNaziv() + "'" +
            "}";
    }
}
