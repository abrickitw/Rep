package org.avd.kamin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.avd.kamin.domain.Evidencija} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvidencijaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nazivEvidencija;

    @NotNull
    private String vrijemeUsluge;

    @NotNull
    private String komentar;

    @NotNull
    private String imeStanara;

    @NotNull
    private String prezimeStanara;

    @NotNull
    private String kontaktStanara;

    private LocalDate datumIspravka;

    private String komentarIspravka;

    private String kucniBroj;

    private UserDTO korisnikIzvrsio;

    private UserDTO korisnikIspravio;

    private RasporedDTO raspored;

    private VrstaUslugeDTO vrstaUsluge;

    private StatusEvidencijeDTO statusEvidencije;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazivEvidencija() {
        return nazivEvidencija;
    }

    public void setNazivEvidencija(String nazivEvidencija) {
        this.nazivEvidencija = nazivEvidencija;
    }

    public String getVrijemeUsluge() {
        return vrijemeUsluge;
    }

    public void setVrijemeUsluge(String vrijemeUsluge) {
        this.vrijemeUsluge = vrijemeUsluge;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getImeStanara() {
        return imeStanara;
    }

    public void setImeStanara(String imeStanara) {
        this.imeStanara = imeStanara;
    }

    public String getPrezimeStanara() {
        return prezimeStanara;
    }

    public void setPrezimeStanara(String prezimeStanara) {
        this.prezimeStanara = prezimeStanara;
    }

    public String getKontaktStanara() {
        return kontaktStanara;
    }

    public void setKontaktStanara(String kontaktStanara) {
        this.kontaktStanara = kontaktStanara;
    }

    public LocalDate getDatumIspravka() {
        return datumIspravka;
    }

    public void setDatumIspravka(LocalDate datumIspravka) {
        this.datumIspravka = datumIspravka;
    }

    public String getKomentarIspravka() {
        return komentarIspravka;
    }

    public void setKomentarIspravka(String komentarIspravka) {
        this.komentarIspravka = komentarIspravka;
    }

    public String getKucniBroj() {
        return kucniBroj;
    }

    public void setKucniBroj(String kucniBroj) {
        this.kucniBroj = kucniBroj;
    }

    public UserDTO getKorisnikIzvrsio() {
        return korisnikIzvrsio;
    }

    public void setKorisnikIzvrsio(UserDTO korisnikIzvrsio) {
        this.korisnikIzvrsio = korisnikIzvrsio;
    }

    public UserDTO getKorisnikIspravio() {
        return korisnikIspravio;
    }

    public void setKorisnikIspravio(UserDTO korisnikIspravio) {
        this.korisnikIspravio = korisnikIspravio;
    }

    public RasporedDTO getRaspored() {
        return raspored;
    }

    public void setRaspored(RasporedDTO raspored) {
        this.raspored = raspored;
    }

    public VrstaUslugeDTO getVrstaUsluge() {
        return vrstaUsluge;
    }

    public void setVrstaUsluge(VrstaUslugeDTO vrstaUsluge) {
        this.vrstaUsluge = vrstaUsluge;
    }

    public StatusEvidencijeDTO getStatusEvidencije() {
        return statusEvidencije;
    }

    public void setStatusEvidencije(StatusEvidencijeDTO statusEvidencije) {
        this.statusEvidencije = statusEvidencije;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvidencijaDTO)) {
            return false;
        }

        EvidencijaDTO evidencijaDTO = (EvidencijaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, evidencijaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvidencijaDTO{" +
            "id=" + getId() +
            ", nazivEvidencija='" + getNazivEvidencija() + "'" +
            ", vrijemeUsluge='" + getVrijemeUsluge() + "'" +
            ", komentar='" + getKomentar() + "'" +
            ", imeStanara='" + getImeStanara() + "'" +
            ", prezimeStanara='" + getPrezimeStanara() + "'" +
            ", kontaktStanara='" + getKontaktStanara() + "'" +
            ", datumIspravka='" + getDatumIspravka() + "'" +
            ", komentarIspravka='" + getKomentarIspravka() + "'" +
            ", kucniBroj='" + getKucniBroj() + "'" +
            ", korisnikIzvrsio=" + getKorisnikIzvrsio() +
            ", korisnikIspravio=" + getKorisnikIspravio() +
            ", raspored=" + getRaspored() +
            ", vrstaUsluge=" + getVrstaUsluge() +
            ", statusEvidencije=" + getStatusEvidencije() +
            "}";
    }
}
