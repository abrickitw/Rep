package org.avd.kamin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UlicaCriteriaTest {

    @Test
    void newUlicaCriteriaHasAllFiltersNullTest() {
        var ulicaCriteria = new UlicaCriteria();
        assertThat(ulicaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void ulicaCriteriaFluentMethodsCreatesFiltersTest() {
        var ulicaCriteria = new UlicaCriteria();

        setAllFilters(ulicaCriteria);

        assertThat(ulicaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void ulicaCriteriaCopyCreatesNullFilterTest() {
        var ulicaCriteria = new UlicaCriteria();
        var copy = ulicaCriteria.copy();

        assertThat(ulicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(ulicaCriteria)
        );
    }

    @Test
    void ulicaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ulicaCriteria = new UlicaCriteria();
        setAllFilters(ulicaCriteria);

        var copy = ulicaCriteria.copy();

        assertThat(ulicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(ulicaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ulicaCriteria = new UlicaCriteria();

        assertThat(ulicaCriteria).hasToString("UlicaCriteria{}");
    }

    private static void setAllFilters(UlicaCriteria ulicaCriteria) {
        ulicaCriteria.id();
        ulicaCriteria.ulicaNaziv();
        ulicaCriteria.gradId();
        ulicaCriteria.naseljeId();
        ulicaCriteria.distinct();
    }

    private static Condition<UlicaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUlicaNaziv()) &&
                condition.apply(criteria.getGradId()) &&
                condition.apply(criteria.getNaseljeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UlicaCriteria> copyFiltersAre(UlicaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUlicaNaziv(), copy.getUlicaNaziv()) &&
                condition.apply(criteria.getGradId(), copy.getGradId()) &&
                condition.apply(criteria.getNaseljeId(), copy.getNaseljeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
