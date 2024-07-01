package org.avd.kamin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StatusEvidencijeCriteriaTest {

    @Test
    void newStatusEvidencijeCriteriaHasAllFiltersNullTest() {
        var statusEvidencijeCriteria = new StatusEvidencijeCriteria();
        assertThat(statusEvidencijeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void statusEvidencijeCriteriaFluentMethodsCreatesFiltersTest() {
        var statusEvidencijeCriteria = new StatusEvidencijeCriteria();

        setAllFilters(statusEvidencijeCriteria);

        assertThat(statusEvidencijeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void statusEvidencijeCriteriaCopyCreatesNullFilterTest() {
        var statusEvidencijeCriteria = new StatusEvidencijeCriteria();
        var copy = statusEvidencijeCriteria.copy();

        assertThat(statusEvidencijeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(statusEvidencijeCriteria)
        );
    }

    @Test
    void statusEvidencijeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var statusEvidencijeCriteria = new StatusEvidencijeCriteria();
        setAllFilters(statusEvidencijeCriteria);

        var copy = statusEvidencijeCriteria.copy();

        assertThat(statusEvidencijeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(statusEvidencijeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var statusEvidencijeCriteria = new StatusEvidencijeCriteria();

        assertThat(statusEvidencijeCriteria).hasToString("StatusEvidencijeCriteria{}");
    }

    private static void setAllFilters(StatusEvidencijeCriteria statusEvidencijeCriteria) {
        statusEvidencijeCriteria.id();
        statusEvidencijeCriteria.statusEvidencijeNaziv();
        statusEvidencijeCriteria.distinct();
    }

    private static Condition<StatusEvidencijeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatusEvidencijeNaziv()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StatusEvidencijeCriteria> copyFiltersAre(
        StatusEvidencijeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatusEvidencijeNaziv(), copy.getStatusEvidencijeNaziv()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
