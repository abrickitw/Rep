package org.avd.kamin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.avd.kamin.domain.Evidencija} entity. This class is used
 * in {@link org.avd.kamin.web.rest.EvidencijaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evidencijas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvidencijaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nazivEvidencija;

    private StringFilter vrijemeUsluge;

    private StringFilter komentar;

    private StringFilter imeStanara;

    private StringFilter prezimeStanara;

    private StringFilter kontaktStanara;

    private LocalDateFilter datumIspravka;

    private StringFilter komentarIspravka;

    private StringFilter kucniBroj;

    private LongFilter korisnikIzvrsioId;

    private LongFilter korisnikIspravioId;

    private LongFilter rasporedId;

    private LongFilter vrstaUslugeId;

    private LongFilter statusEvidencijeId;

    private Boolean distinct;

    public EvidencijaCriteria() {}

    public EvidencijaCriteria(EvidencijaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nazivEvidencija = other.optionalNazivEvidencija().map(StringFilter::copy).orElse(null);
        this.vrijemeUsluge = other.optionalVrijemeUsluge().map(StringFilter::copy).orElse(null);
        this.komentar = other.optionalKomentar().map(StringFilter::copy).orElse(null);
        this.imeStanara = other.optionalImeStanara().map(StringFilter::copy).orElse(null);
        this.prezimeStanara = other.optionalPrezimeStanara().map(StringFilter::copy).orElse(null);
        this.kontaktStanara = other.optionalKontaktStanara().map(StringFilter::copy).orElse(null);
        this.datumIspravka = other.optionalDatumIspravka().map(LocalDateFilter::copy).orElse(null);
        this.komentarIspravka = other.optionalKomentarIspravka().map(StringFilter::copy).orElse(null);
        this.kucniBroj = other.optionalKucniBroj().map(StringFilter::copy).orElse(null);
        this.korisnikIzvrsioId = other.optionalKorisnikIzvrsioId().map(LongFilter::copy).orElse(null);
        this.korisnikIspravioId = other.optionalKorisnikIspravioId().map(LongFilter::copy).orElse(null);
        this.rasporedId = other.optionalRasporedId().map(LongFilter::copy).orElse(null);
        this.vrstaUslugeId = other.optionalVrstaUslugeId().map(LongFilter::copy).orElse(null);
        this.statusEvidencijeId = other.optionalStatusEvidencijeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EvidencijaCriteria copy() {
        return new EvidencijaCriteria(this);
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

    public StringFilter getNazivEvidencija() {
        return nazivEvidencija;
    }

    public Optional<StringFilter> optionalNazivEvidencija() {
        return Optional.ofNullable(nazivEvidencija);
    }

    public StringFilter nazivEvidencija() {
        if (nazivEvidencija == null) {
            setNazivEvidencija(new StringFilter());
        }
        return nazivEvidencija;
    }

    public void setNazivEvidencija(StringFilter nazivEvidencija) {
        this.nazivEvidencija = nazivEvidencija;
    }

    public StringFilter getVrijemeUsluge() {
        return vrijemeUsluge;
    }

    public Optional<StringFilter> optionalVrijemeUsluge() {
        return Optional.ofNullable(vrijemeUsluge);
    }

    public StringFilter vrijemeUsluge() {
        if (vrijemeUsluge == null) {
            setVrijemeUsluge(new StringFilter());
        }
        return vrijemeUsluge;
    }

    public void setVrijemeUsluge(StringFilter vrijemeUsluge) {
        this.vrijemeUsluge = vrijemeUsluge;
    }

    public StringFilter getKomentar() {
        return komentar;
    }

    public Optional<StringFilter> optionalKomentar() {
        return Optional.ofNullable(komentar);
    }

    public StringFilter komentar() {
        if (komentar == null) {
            setKomentar(new StringFilter());
        }
        return komentar;
    }

    public void setKomentar(StringFilter komentar) {
        this.komentar = komentar;
    }

    public StringFilter getImeStanara() {
        return imeStanara;
    }

    public Optional<StringFilter> optionalImeStanara() {
        return Optional.ofNullable(imeStanara);
    }

    public StringFilter imeStanara() {
        if (imeStanara == null) {
            setImeStanara(new StringFilter());
        }
        return imeStanara;
    }

    public void setImeStanara(StringFilter imeStanara) {
        this.imeStanara = imeStanara;
    }

    public StringFilter getPrezimeStanara() {
        return prezimeStanara;
    }

    public Optional<StringFilter> optionalPrezimeStanara() {
        return Optional.ofNullable(prezimeStanara);
    }

    public StringFilter prezimeStanara() {
        if (prezimeStanara == null) {
            setPrezimeStanara(new StringFilter());
        }
        return prezimeStanara;
    }

    public void setPrezimeStanara(StringFilter prezimeStanara) {
        this.prezimeStanara = prezimeStanara;
    }

    public StringFilter getKontaktStanara() {
        return kontaktStanara;
    }

    public Optional<StringFilter> optionalKontaktStanara() {
        return Optional.ofNullable(kontaktStanara);
    }

    public StringFilter kontaktStanara() {
        if (kontaktStanara == null) {
            setKontaktStanara(new StringFilter());
        }
        return kontaktStanara;
    }

    public void setKontaktStanara(StringFilter kontaktStanara) {
        this.kontaktStanara = kontaktStanara;
    }

    public LocalDateFilter getDatumIspravka() {
        return datumIspravka;
    }

    public Optional<LocalDateFilter> optionalDatumIspravka() {
        return Optional.ofNullable(datumIspravka);
    }

    public LocalDateFilter datumIspravka() {
        if (datumIspravka == null) {
            setDatumIspravka(new LocalDateFilter());
        }
        return datumIspravka;
    }

    public void setDatumIspravka(LocalDateFilter datumIspravka) {
        this.datumIspravka = datumIspravka;
    }

    public StringFilter getKomentarIspravka() {
        return komentarIspravka;
    }

    public Optional<StringFilter> optionalKomentarIspravka() {
        return Optional.ofNullable(komentarIspravka);
    }

    public StringFilter komentarIspravka() {
        if (komentarIspravka == null) {
            setKomentarIspravka(new StringFilter());
        }
        return komentarIspravka;
    }

    public void setKomentarIspravka(StringFilter komentarIspravka) {
        this.komentarIspravka = komentarIspravka;
    }

    public StringFilter getKucniBroj() {
        return kucniBroj;
    }

    public Optional<StringFilter> optionalKucniBroj() {
        return Optional.ofNullable(kucniBroj);
    }

    public StringFilter kucniBroj() {
        if (kucniBroj == null) {
            setKucniBroj(new StringFilter());
        }
        return kucniBroj;
    }

    public void setKucniBroj(StringFilter kucniBroj) {
        this.kucniBroj = kucniBroj;
    }

    public LongFilter getKorisnikIzvrsioId() {
        return korisnikIzvrsioId;
    }

    public Optional<LongFilter> optionalKorisnikIzvrsioId() {
        return Optional.ofNullable(korisnikIzvrsioId);
    }

    public LongFilter korisnikIzvrsioId() {
        if (korisnikIzvrsioId == null) {
            setKorisnikIzvrsioId(new LongFilter());
        }
        return korisnikIzvrsioId;
    }

    public void setKorisnikIzvrsioId(LongFilter korisnikIzvrsioId) {
        this.korisnikIzvrsioId = korisnikIzvrsioId;
    }

    public LongFilter getKorisnikIspravioId() {
        return korisnikIspravioId;
    }

    public Optional<LongFilter> optionalKorisnikIspravioId() {
        return Optional.ofNullable(korisnikIspravioId);
    }

    public LongFilter korisnikIspravioId() {
        if (korisnikIspravioId == null) {
            setKorisnikIspravioId(new LongFilter());
        }
        return korisnikIspravioId;
    }

    public void setKorisnikIspravioId(LongFilter korisnikIspravioId) {
        this.korisnikIspravioId = korisnikIspravioId;
    }

    public LongFilter getRasporedId() {
        return rasporedId;
    }

    public Optional<LongFilter> optionalRasporedId() {
        return Optional.ofNullable(rasporedId);
    }

    public LongFilter rasporedId() {
        if (rasporedId == null) {
            setRasporedId(new LongFilter());
        }
        return rasporedId;
    }

    public void setRasporedId(LongFilter rasporedId) {
        this.rasporedId = rasporedId;
    }

    public LongFilter getVrstaUslugeId() {
        return vrstaUslugeId;
    }

    public Optional<LongFilter> optionalVrstaUslugeId() {
        return Optional.ofNullable(vrstaUslugeId);
    }

    public LongFilter vrstaUslugeId() {
        if (vrstaUslugeId == null) {
            setVrstaUslugeId(new LongFilter());
        }
        return vrstaUslugeId;
    }

    public void setVrstaUslugeId(LongFilter vrstaUslugeId) {
        this.vrstaUslugeId = vrstaUslugeId;
    }

    public LongFilter getStatusEvidencijeId() {
        return statusEvidencijeId;
    }

    public Optional<LongFilter> optionalStatusEvidencijeId() {
        return Optional.ofNullable(statusEvidencijeId);
    }

    public LongFilter statusEvidencijeId() {
        if (statusEvidencijeId == null) {
            setStatusEvidencijeId(new LongFilter());
        }
        return statusEvidencijeId;
    }

    public void setStatusEvidencijeId(LongFilter statusEvidencijeId) {
        this.statusEvidencijeId = statusEvidencijeId;
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
        final EvidencijaCriteria that = (EvidencijaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nazivEvidencija, that.nazivEvidencija) &&
            Objects.equals(vrijemeUsluge, that.vrijemeUsluge) &&
            Objects.equals(komentar, that.komentar) &&
            Objects.equals(imeStanara, that.imeStanara) &&
            Objects.equals(prezimeStanara, that.prezimeStanara) &&
            Objects.equals(kontaktStanara, that.kontaktStanara) &&
            Objects.equals(datumIspravka, that.datumIspravka) &&
            Objects.equals(komentarIspravka, that.komentarIspravka) &&
            Objects.equals(kucniBroj, that.kucniBroj) &&
            Objects.equals(korisnikIzvrsioId, that.korisnikIzvrsioId) &&
            Objects.equals(korisnikIspravioId, that.korisnikIspravioId) &&
            Objects.equals(rasporedId, that.rasporedId) &&
            Objects.equals(vrstaUslugeId, that.vrstaUslugeId) &&
            Objects.equals(statusEvidencijeId, that.statusEvidencijeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nazivEvidencija,
            vrijemeUsluge,
            komentar,
            imeStanara,
            prezimeStanara,
            kontaktStanara,
            datumIspravka,
            komentarIspravka,
            kucniBroj,
            korisnikIzvrsioId,
            korisnikIspravioId,
            rasporedId,
            vrstaUslugeId,
            statusEvidencijeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvidencijaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNazivEvidencija().map(f -> "nazivEvidencija=" + f + ", ").orElse("") +
            optionalVrijemeUsluge().map(f -> "vrijemeUsluge=" + f + ", ").orElse("") +
            optionalKomentar().map(f -> "komentar=" + f + ", ").orElse("") +
            optionalImeStanara().map(f -> "imeStanara=" + f + ", ").orElse("") +
            optionalPrezimeStanara().map(f -> "prezimeStanara=" + f + ", ").orElse("") +
            optionalKontaktStanara().map(f -> "kontaktStanara=" + f + ", ").orElse("") +
            optionalDatumIspravka().map(f -> "datumIspravka=" + f + ", ").orElse("") +
            optionalKomentarIspravka().map(f -> "komentarIspravka=" + f + ", ").orElse("") +
            optionalKucniBroj().map(f -> "kucniBroj=" + f + ", ").orElse("") +
            optionalKorisnikIzvrsioId().map(f -> "korisnikIzvrsioId=" + f + ", ").orElse("") +
            optionalKorisnikIspravioId().map(f -> "korisnikIspravioId=" + f + ", ").orElse("") +
            optionalRasporedId().map(f -> "rasporedId=" + f + ", ").orElse("") +
            optionalVrstaUslugeId().map(f -> "vrstaUslugeId=" + f + ", ").orElse("") +
            optionalStatusEvidencijeId().map(f -> "statusEvidencijeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
