package org.avd.kamin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EvidencijaCriteriaTest {

    @Test
    void newEvidencijaCriteriaHasAllFiltersNullTest() {
        var evidencijaCriteria = new EvidencijaCriteria();
        assertThat(evidencijaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void evidencijaCriteriaFluentMethodsCreatesFiltersTest() {
        var evidencijaCriteria = new EvidencijaCriteria();

        setAllFilters(evidencijaCriteria);

        assertThat(evidencijaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void evidencijaCriteriaCopyCreatesNullFilterTest() {
        var evidencijaCriteria = new EvidencijaCriteria();
        var copy = evidencijaCriteria.copy();

        assertThat(evidencijaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(evidencijaCriteria)
        );
    }

    @Test
    void evidencijaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var evidencijaCriteria = new EvidencijaCriteria();
        setAllFilters(evidencijaCriteria);

        var copy = evidencijaCriteria.copy();

        assertThat(evidencijaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(evidencijaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var evidencijaCriteria = new EvidencijaCriteria();

        assertThat(evidencijaCriteria).hasToString("EvidencijaCriteria{}");
    }

    private static void setAllFilters(EvidencijaCriteria evidencijaCriteria) {
        evidencijaCriteria.id();
        evidencijaCriteria.nazivEvidencija();
        evidencijaCriteria.vrijemeUsluge();
        evidencijaCriteria.komentar();
        evidencijaCriteria.imeStanara();
        evidencijaCriteria.prezimeStanara();
        evidencijaCriteria.kontaktStanara();
        evidencijaCriteria.datumIspravka();
        evidencijaCriteria.komentarIspravka();
        evidencijaCriteria.kucniBroj();
        evidencijaCriteria.korisnikIzvrsioId();
        evidencijaCriteria.korisnikIspravioId();
        evidencijaCriteria.rasporedId();
        evidencijaCriteria.vrstaUslugeId();
        evidencijaCriteria.statusEvidencijeId();
        evidencijaCriteria.distinct();
    }

    private static Condition<EvidencijaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNazivEvidencija()) &&
                condition.apply(criteria.getVrijemeUsluge()) &&
                condition.apply(criteria.getKomentar()) &&
                condition.apply(criteria.getImeStanara()) &&
                condition.apply(criteria.getPrezimeStanara()) &&
                condition.apply(criteria.getKontaktStanara()) &&
                condition.apply(criteria.getDatumIspravka()) &&
                condition.apply(criteria.getKomentarIspravka()) &&
                condition.apply(criteria.getKucniBroj()) &&
                condition.apply(criteria.getKorisnikIzvrsioId()) &&
                condition.apply(criteria.getKorisnikIspravioId()) &&
                condition.apply(criteria.getRasporedId()) &&
                condition.apply(criteria.getVrstaUslugeId()) &&
                condition.apply(criteria.getStatusEvidencijeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EvidencijaCriteria> copyFiltersAre(EvidencijaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNazivEvidencija(), copy.getNazivEvidencija()) &&
                condition.apply(criteria.getVrijemeUsluge(), copy.getVrijemeUsluge()) &&
                condition.apply(criteria.getKomentar(), copy.getKomentar()) &&
                condition.apply(criteria.getImeStanara(), copy.getImeStanara()) &&
                condition.apply(criteria.getPrezimeStanara(), copy.getPrezimeStanara()) &&
                condition.apply(criteria.getKontaktStanara(), copy.getKontaktStanara()) &&
                condition.apply(criteria.getDatumIspravka(), copy.getDatumIspravka()) &&
                condition.apply(criteria.getKomentarIspravka(), copy.getKomentarIspravka()) &&
                condition.apply(criteria.getKucniBroj(), copy.getKucniBroj()) &&
                condition.apply(criteria.getKorisnikIzvrsioId(), copy.getKorisnikIzvrsioId()) &&
                condition.apply(criteria.getKorisnikIspravioId(), copy.getKorisnikIspravioId()) &&
                condition.apply(criteria.getRasporedId(), copy.getRasporedId()) &&
                condition.apply(criteria.getVrstaUslugeId(), copy.getVrstaUslugeId()) &&
                condition.apply(criteria.getStatusEvidencijeId(), copy.getStatusEvidencijeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
