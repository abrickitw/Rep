package org.avd.kamin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.avd.kamin.domain.Raspored} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RasporedDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate datumUsluge;

    private GradDTO grad;

    private NaseljeDTO naselje;

    private UlicaDTO ulica;

    private UserDTO korisnikKreirao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatumUsluge() {
        return datumUsluge;
    }

    public void setDatumUsluge(LocalDate datumUsluge) {
        this.datumUsluge = datumUsluge;
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

    public UlicaDTO getUlica() {
        return ulica;
    }

    public void setUlica(UlicaDTO ulica) {
        this.ulica = ulica;
    }

    public UserDTO getKorisnikKreirao() {
        return korisnikKreirao;
    }

    public void setKorisnikKreirao(UserDTO korisnikKreirao) {
        this.korisnikKreirao = korisnikKreirao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RasporedDTO)) {
            return false;
        }

        RasporedDTO rasporedDTO = (RasporedDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rasporedDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RasporedDTO{" +
            "id=" + getId() +
            ", datumUsluge='" + getDatumUsluge() + "'" +
            ", grad=" + getGrad() +
            ", naselje=" + getNaselje() +
            ", ulica=" + getUlica() +
            ", korisnikKreirao=" + getKorisnikKreirao() +
            "}";
    }
}
