package org.avd.kamin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.StatusEvidencijeTestSamples.*;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusEvidencijeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusEvidencije.class);
        StatusEvidencije statusEvidencije1 = getStatusEvidencijeSample1();
        StatusEvidencije statusEvidencije2 = new StatusEvidencije();
        assertThat(statusEvidencije1).isNotEqualTo(statusEvidencije2);

        statusEvidencije2.setId(statusEvidencije1.getId());
        assertThat(statusEvidencije1).isEqualTo(statusEvidencije2);

        statusEvidencije2 = getStatusEvidencijeSample2();
        assertThat(statusEvidencije1).isNotEqualTo(statusEvidencije2);
    }
}
