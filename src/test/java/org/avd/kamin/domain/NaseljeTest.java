package org.avd.kamin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.GradTestSamples.*;
import static org.avd.kamin.domain.NaseljeTestSamples.*;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NaseljeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Naselje.class);
        Naselje naselje1 = getNaseljeSample1();
        Naselje naselje2 = new Naselje();
        assertThat(naselje1).isNotEqualTo(naselje2);

        naselje2.setId(naselje1.getId());
        assertThat(naselje1).isEqualTo(naselje2);

        naselje2 = getNaseljeSample2();
        assertThat(naselje1).isNotEqualTo(naselje2);
    }

    @Test
    void gradTest() {
        Naselje naselje = getNaseljeRandomSampleGenerator();
        Grad gradBack = getGradRandomSampleGenerator();

        naselje.setGrad(gradBack);
        assertThat(naselje.getGrad()).isEqualTo(gradBack);

        naselje.grad(null);
        assertThat(naselje.getGrad()).isNull();
    }
}
