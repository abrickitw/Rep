package org.avd.kamin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Evidencija.
 */
@Entity
@Table(name = "evidencija")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evidencija implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "naziv_evidencija", nullable = false, unique = true)
    private String nazivEvidencija;

    @NotNull
    @Column(name = "vrijeme_usluge", nullable = false)
    private String vrijemeUsluge;

    @NotNull
    @Column(name = "komentar", nullable = false)
    private String komentar;

    @NotNull
    @Column(name = "ime_stanara", nullable = false)
    private String imeStanara;

    @NotNull
    @Column(name = "prezime_stanara", nullable = false)
    private String prezimeStanara;

    @NotNull
    @Column(name = "kontakt_stanara", nullable = false)
    private String kontaktStanara;

    @Column(name = "datum_ispravka")
    private LocalDate datumIspravka;

    @Column(name = "komentar_ispravka")
    private String komentarIspravka;

    @Column(name = "kucni_broj")
    private String kucniBroj;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User korisnikIzvrsio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User korisnikIspravio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "grad", "naselje", "ulica", "korisnikKreirao" }, allowSetters = true)
    private Raspored raspored;

    @ManyToOne(fetch = FetchType.LAZY)
    private VrstaUsluge vrstaUsluge;

    @ManyToOne(fetch = FetchType.LAZY)
    private StatusEvidencije statusEvidencije;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evidencija id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazivEvidencija() {
        return this.nazivEvidencija;
    }

    public Evidencija nazivEvidencija(String nazivEvidencija) {
        this.setNazivEvidencija(nazivEvidencija);
        return this;
    }

    public void setNazivEvidencija(String nazivEvidencija) {
        this.nazivEvidencija = nazivEvidencija;
    }

    public String getVrijemeUsluge() {
        return this.vrijemeUsluge;
    }

    public Evidencija vrijemeUsluge(String vrijemeUsluge) {
        this.setVrijemeUsluge(vrijemeUsluge);
        return this;
    }

    public void setVrijemeUsluge(String vrijemeUsluge) {
        this.vrijemeUsluge = vrijemeUsluge;
    }

    public String getKomentar() {
        return this.komentar;
    }

    public Evidencija komentar(String komentar) {
        this.setKomentar(komentar);
        return this;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getImeStanara() {
        return this.imeStanara;
    }

    public Evidencija imeStanara(String imeStanara) {
        this.setImeStanara(imeStanara);
        return this;
    }

    public void setImeStanara(String imeStanara) {
        this.imeStanara = imeStanara;
    }

    public String getPrezimeStanara() {
        return this.prezimeStanara;
    }

    public Evidencija prezimeStanara(String prezimeStanara) {
        this.setPrezimeStanara(prezimeStanara);
        return this;
    }

    public void setPrezimeStanara(String prezimeStanara) {
        this.prezimeStanara = prezimeStanara;
    }

    public String getKontaktStanara() {
        return this.kontaktStanara;
    }

    public Evidencija kontaktStanara(String kontaktStanara) {
        this.setKontaktStanara(kontaktStanara);
        return this;
    }

    public void setKontaktStanara(String kontaktStanara) {
        this.kontaktStanara = kontaktStanara;
    }

    public LocalDate getDatumIspravka() {
        return this.datumIspravka;
    }

    public Evidencija datumIspravka(LocalDate datumIspravka) {
        this.setDatumIspravka(datumIspravka);
        return this;
    }

    public void setDatumIspravka(LocalDate datumIspravka) {
        this.datumIspravka = datumIspravka;
    }

    public String getKomentarIspravka() {
        return this.komentarIspravka;
    }

    public Evidencija komentarIspravka(String komentarIspravka) {
        this.setKomentarIspravka(komentarIspravka);
        return this;
    }

    public void setKomentarIspravka(String komentarIspravka) {
        this.komentarIspravka = komentarIspravka;
    }

    public String getKucniBroj() {
        return this.kucniBroj;
    }

    public Evidencija kucniBroj(String kucniBroj) {
        this.setKucniBroj(kucniBroj);
        return this;
    }

    public void setKucniBroj(String kucniBroj) {
        this.kucniBroj = kucniBroj;
    }

    public User getKorisnikIzvrsio() {
        return this.korisnikIzvrsio;
    }

    public void setKorisnikIzvrsio(User user) {
        this.korisnikIzvrsio = user;
    }

    public Evidencija korisnikIzvrsio(User user) {
        this.setKorisnikIzvrsio(user);
        return this;
    }

    public User getKorisnikIspravio() {
        return this.korisnikIspravio;
    }

    public void setKorisnikIspravio(User user) {
        this.korisnikIspravio = user;
    }

    public Evidencija korisnikIspravio(User user) {
        this.setKorisnikIspravio(user);
        return this;
    }

    public Raspored getRaspored() {
        return this.raspored;
    }

    public void setRaspored(Raspored raspored) {
        this.raspored = raspored;
    }

    public Evidencija raspored(Raspored raspored) {
        this.setRaspored(raspored);
        return this;
    }

    public VrstaUsluge getVrstaUsluge() {
        return this.vrstaUsluge;
    }

    public void setVrstaUsluge(VrstaUsluge vrstaUsluge) {
        this.vrstaUsluge = vrstaUsluge;
    }

    public Evidencija vrstaUsluge(VrstaUsluge vrstaUsluge) {
        this.setVrstaUsluge(vrstaUsluge);
        return this;
    }

    public StatusEvidencije getStatusEvidencije() {
        return this.statusEvidencije;
    }

    public void setStatusEvidencije(StatusEvidencije statusEvidencije) {
        this.statusEvidencije = statusEvidencije;
    }

    public Evidencija statusEvidencije(StatusEvidencije statusEvidencije) {
        this.setStatusEvidencije(statusEvidencije);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evidencija)) {
            return false;
        }
        return getId() != null && getId().equals(((Evidencija) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evidencija{" +
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
            "}";
    }
}
