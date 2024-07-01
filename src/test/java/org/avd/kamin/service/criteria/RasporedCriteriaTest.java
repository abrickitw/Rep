package org.avd.kamin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RasporedCriteriaTest {

    @Test
    void newRasporedCriteriaHasAllFiltersNullTest() {
        var rasporedCriteria = new RasporedCriteria();
        assertThat(rasporedCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void rasporedCriteriaFluentMethodsCreatesFiltersTest() {
        var rasporedCriteria = new RasporedCriteria();

        setAllFilters(rasporedCriteria);

        assertThat(rasporedCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void rasporedCriteriaCopyCreatesNullFilterTest() {
        var rasporedCriteria = new RasporedCriteria();
        var copy = rasporedCriteria.copy();

        assertThat(rasporedCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(rasporedCriteria)
        );
    }

    @Test
    void rasporedCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var rasporedCriteria = new RasporedCriteria();
        setAllFilters(rasporedCriteria);

        var copy = rasporedCriteria.copy();

        assertThat(rasporedCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(rasporedCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var rasporedCriteria = new RasporedCriteria();

        assertThat(rasporedCriteria).hasToString("RasporedCriteria{}");
    }

    private static void setAllFilters(RasporedCriteria rasporedCriteria) {
        rasporedCriteria.id();
        rasporedCriteria.datumUsluge();
        rasporedCriteria.gradId();
        rasporedCriteria.naseljeId();
        rasporedCriteria.ulicaId();
        rasporedCriteria.korisnikKreiraoId();
        rasporedCriteria.distinct();
    }

    private static Condition<RasporedCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDatumUsluge()) &&
                condition.apply(criteria.getGradId()) &&
                condition.apply(criteria.getNaseljeId()) &&
                condition.apply(criteria.getUlicaId()) &&
                condition.apply(criteria.getKorisnikKreiraoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RasporedCriteria> copyFiltersAre(RasporedCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDatumUsluge(), copy.getDatumUsluge()) &&
                condition.apply(criteria.getGradId(), copy.getGradId()) &&
                condition.apply(criteria.getNaseljeId(), copy.getNaseljeId()) &&
                condition.apply(criteria.getUlicaId(), copy.getUlicaId()) &&
                condition.apply(criteria.getKorisnikKreiraoId(), copy.getKorisnikKreiraoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
