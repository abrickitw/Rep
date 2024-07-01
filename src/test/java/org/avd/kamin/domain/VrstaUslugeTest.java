package org.avd.kamin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.VrstaUslugeTestSamples.*;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VrstaUslugeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VrstaUsluge.class);
        VrstaUsluge vrstaUsluge1 = getVrstaUslugeSample1();
        VrstaUsluge vrstaUsluge2 = new VrstaUsluge();
        assertThat(vrstaUsluge1).isNotEqualTo(vrstaUsluge2);

        vrstaUsluge2.setId(vrstaUsluge1.getId());
        assertThat(vrstaUsluge1).isEqualTo(vrstaUsluge2);

        vrstaUsluge2 = getVrstaUslugeSample2();
        assertThat(vrstaUsluge1).isNotEqualTo(vrstaUsluge2);
    }
}
