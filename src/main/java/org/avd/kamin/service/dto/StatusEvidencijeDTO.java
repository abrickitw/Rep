package org.avd.kamin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.avd.kamin.domain.StatusEvidencije} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusEvidencijeDTO implements Serializable {

    private Long id;

    @NotNull
    private String statusEvidencijeNaziv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusEvidencijeNaziv() {
        return statusEvidencijeNaziv;
    }

    public void setStatusEvidencijeNaziv(String statusEvidencijeNaziv) {
        this.statusEvidencijeNaziv = statusEvidencijeNaziv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusEvidencijeDTO)) {
            return false;
        }

        StatusEvidencijeDTO statusEvidencijeDTO = (StatusEvidencijeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, statusEvidencijeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusEvidencijeDTO{" +
            "id=" + getId() +
            ", statusEvidencijeNaziv='" + getStatusEvidencijeNaziv() + "'" +
            "}";
    }
}
