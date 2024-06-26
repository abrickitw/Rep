package org.avd.kamin.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NaseljeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNaseljeAllPropertiesEquals(Naselje expected, Naselje actual) {
        assertNaseljeAutoGeneratedPropertiesEquals(expected, actual);
        assertNaseljeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNaseljeAllUpdatablePropertiesEquals(Naselje expected, Naselje actual) {
        assertNaseljeUpdatableFieldsEquals(expected, actual);
        assertNaseljeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNaseljeAutoGeneratedPropertiesEquals(Naselje expected, Naselje actual) {
        assertThat(expected)
            .as("Verify Naselje auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNaseljeUpdatableFieldsEquals(Naselje expected, Naselje actual) {
        assertThat(expected)
            .as("Verify Naselje relevant properties")
            .satisfies(e -> assertThat(e.getNaseljeNaziv()).as("check naseljeNaziv").isEqualTo(actual.getNaseljeNaziv()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNaseljeUpdatableRelationshipsEquals(Naselje expected, Naselje actual) {
        assertThat(expected)
            .as("Verify Naselje relationships")
            .satisfies(e -> assertThat(e.getGrad()).as("check grad").isEqualTo(actual.getGrad()));
    }
}
