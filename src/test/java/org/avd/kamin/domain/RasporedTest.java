package org.avd.kamin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.GradTestSamples.*;
import static org.avd.kamin.domain.NaseljeTestSamples.*;
import static org.avd.kamin.domain.RasporedTestSamples.*;
import static org.avd.kamin.domain.UlicaTestSamples.*;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RasporedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Raspored.class);
        Raspored raspored1 = getRasporedSample1();
        Raspored raspored2 = new Raspored();
        assertThat(raspored1).isNotEqualTo(raspored2);

        raspored2.setId(raspored1.getId());
        assertThat(raspored1).isEqualTo(raspored2);

        raspored2 = getRasporedSample2();
        assertThat(raspored1).isNotEqualTo(raspored2);
    }

    @Test
    void gradTest() {
        Raspored raspored = getRasporedRandomSampleGenerator();
        Grad gradBack = getGradRandomSampleGenerator();

        raspored.setGrad(gradBack);
        assertThat(raspored.getGrad()).isEqualTo(gradBack);

        raspored.grad(null);
        assertThat(raspored.getGrad()).isNull();
    }

    @Test
    void naseljeTest() {
        Raspored raspored = getRasporedRandomSampleGenerator();
        Naselje naseljeBack = getNaseljeRandomSampleGenerator();

        raspored.setNaselje(naseljeBack);
        assertThat(raspored.getNaselje()).isEqualTo(naseljeBack);

        raspored.naselje(null);
        assertThat(raspored.getNaselje()).isNull();
    }

    @Test
    void ulicaTest() {
        Raspored raspored = getRasporedRandomSampleGenerator();
        Ulica ulicaBack = getUlicaRandomSampleGenerator();

        raspored.setUlica(ulicaBack);
        assertThat(raspored.getUlica()).isEqualTo(ulicaBack);

        raspored.ulica(null);
        assertThat(raspored.getUlica()).isNull();
    }
}
