package org.avd.kamin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GradCriteriaTest {

    @Test
    void newGradCriteriaHasAllFiltersNullTest() {
        var gradCriteria = new GradCriteria();
        assertThat(gradCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void gradCriteriaFluentMethodsCreatesFiltersTest() {
        var gradCriteria = new GradCriteria();

        setAllFilters(gradCriteria);

        assertThat(gradCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void gradCriteriaCopyCreatesNullFilterTest() {
        var gradCriteria = new GradCriteria();
        var copy = gradCriteria.copy();

        assertThat(gradCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(gradCriteria)
        );
    }

    @Test
    void gradCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var gradCriteria = new GradCriteria();
        setAllFilters(gradCriteria);

        var copy = gradCriteria.copy();

        assertThat(gradCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(gradCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var gradCriteria = new GradCriteria();

        assertThat(gradCriteria).hasToString("GradCriteria{}");
    }

    private static void setAllFilters(GradCriteria gradCriteria) {
        gradCriteria.id();
        gradCriteria.gradNaziv();
        gradCriteria.distinct();
    }

    private static Condition<GradCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getGradNaziv()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GradCriteria> copyFiltersAre(GradCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getGradNaziv(), copy.getGradNaziv()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
