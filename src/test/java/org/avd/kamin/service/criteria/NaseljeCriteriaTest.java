package org.avd.kamin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NaseljeCriteriaTest {

    @Test
    void newNaseljeCriteriaHasAllFiltersNullTest() {
        var naseljeCriteria = new NaseljeCriteria();
        assertThat(naseljeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void naseljeCriteriaFluentMethodsCreatesFiltersTest() {
        var naseljeCriteria = new NaseljeCriteria();

        setAllFilters(naseljeCriteria);

        assertThat(naseljeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void naseljeCriteriaCopyCreatesNullFilterTest() {
        var naseljeCriteria = new NaseljeCriteria();
        var copy = naseljeCriteria.copy();

        assertThat(naseljeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(naseljeCriteria)
        );
    }

    @Test
    void naseljeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var naseljeCriteria = new NaseljeCriteria();
        setAllFilters(naseljeCriteria);

        var copy = naseljeCriteria.copy();

        assertThat(naseljeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(naseljeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var naseljeCriteria = new NaseljeCriteria();

        assertThat(naseljeCriteria).hasToString("NaseljeCriteria{}");
    }

    private static void setAllFilters(NaseljeCriteria naseljeCriteria) {
        naseljeCriteria.id();
        naseljeCriteria.naseljeNaziv();
        naseljeCriteria.gradId();
        naseljeCriteria.distinct();
    }

    private static Condition<NaseljeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNaseljeNaziv()) &&
                condition.apply(criteria.getGradId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NaseljeCriteria> copyFiltersAre(NaseljeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNaseljeNaziv(), copy.getNaseljeNaziv()) &&
                condition.apply(criteria.getGradId(), copy.getGradId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
