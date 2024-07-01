package org.avd.kamin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.GradTestSamples.*;
import static org.avd.kamin.domain.NaseljeTestSamples.*;
import static org.avd.kamin.domain.UlicaTestSamples.*;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UlicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ulica.class);
        Ulica ulica1 = getUlicaSample1();
        Ulica ulica2 = new Ulica();
        assertThat(ulica1).isNotEqualTo(ulica2);

        ulica2.setId(ulica1.getId());
        assertThat(ulica1).isEqualTo(ulica2);

        ulica2 = getUlicaSample2();
        assertThat(ulica1).isNotEqualTo(ulica2);
    }

    @Test
    void gradTest() {
        Ulica ulica = getUlicaRandomSampleGenerator();
        Grad gradBack = getGradRandomSampleGenerator();

        ulica.setGrad(gradBack);
        assertThat(ulica.getGrad()).isEqualTo(gradBack);

        ulica.grad(null);
        assertThat(ulica.getGrad()).isNull();
    }

    @Test
    void naseljeTest() {
        Ulica ulica = getUlicaRandomSampleGenerator();
        Naselje naseljeBack = getNaseljeRandomSampleGenerator();

        ulica.setNaselje(naseljeBack);
        assertThat(ulica.getNaselje()).isEqualTo(naseljeBack);

        ulica.naselje(null);
        assertThat(ulica.getNaselje()).isNull();
    }
}
