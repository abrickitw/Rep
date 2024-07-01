package org.avd.kamin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VrstaUslugeCriteriaTest {

    @Test
    void newVrstaUslugeCriteriaHasAllFiltersNullTest() {
        var vrstaUslugeCriteria = new VrstaUslugeCriteria();
        assertThat(vrstaUslugeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void vrstaUslugeCriteriaFluentMethodsCreatesFiltersTest() {
        var vrstaUslugeCriteria = new VrstaUslugeCriteria();

        setAllFilters(vrstaUslugeCriteria);

        assertThat(vrstaUslugeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void vrstaUslugeCriteriaCopyCreatesNullFilterTest() {
        var vrstaUslugeCriteria = new VrstaUslugeCriteria();
        var copy = vrstaUslugeCriteria.copy();

        assertThat(vrstaUslugeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(vrstaUslugeCriteria)
        );
    }

    @Test
    void vrstaUslugeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vrstaUslugeCriteria = new VrstaUslugeCriteria();
        setAllFilters(vrstaUslugeCriteria);

        var copy = vrstaUslugeCriteria.copy();

        assertThat(vrstaUslugeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(vrstaUslugeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vrstaUslugeCriteria = new VrstaUslugeCriteria();

        assertThat(vrstaUslugeCriteria).hasToString("VrstaUslugeCriteria{}");
    }

    private static void setAllFilters(VrstaUslugeCriteria vrstaUslugeCriteria) {
        vrstaUslugeCriteria.id();
        vrstaUslugeCriteria.vrstaUslugeNaziv();
        vrstaUslugeCriteria.distinct();
    }

    private static Condition<VrstaUslugeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVrstaUslugeNaziv()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VrstaUslugeCriteria> copyFiltersAre(VrstaUslugeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVrstaUslugeNaziv(), copy.getVrstaUslugeNaziv()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
