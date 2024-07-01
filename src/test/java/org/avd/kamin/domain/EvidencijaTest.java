package org.avd.kamin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.EvidencijaTestSamples.*;
import static org.avd.kamin.domain.RasporedTestSamples.*;
import static org.avd.kamin.domain.StatusEvidencijeTestSamples.*;
import static org.avd.kamin.domain.VrstaUslugeTestSamples.*;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvidencijaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evidencija.class);
        Evidencija evidencija1 = getEvidencijaSample1();
        Evidencija evidencija2 = new Evidencija();
        assertThat(evidencija1).isNotEqualTo(evidencija2);

        evidencija2.setId(evidencija1.getId());
        assertThat(evidencija1).isEqualTo(evidencija2);

        evidencija2 = getEvidencijaSample2();
        assertThat(evidencija1).isNotEqualTo(evidencija2);
    }

    @Test
    void rasporedTest() {
        Evidencija evidencija = getEvidencijaRandomSampleGenerator();
        Raspored rasporedBack = getRasporedRandomSampleGenerator();

        evidencija.setRaspored(rasporedBack);
        assertThat(evidencija.getRaspored()).isEqualTo(rasporedBack);

        evidencija.raspored(null);
        assertThat(evidencija.getRaspored()).isNull();
    }

    @Test
    void vrstaUslugeTest() {
        Evidencija evidencija = getEvidencijaRandomSampleGenerator();
        VrstaUsluge vrstaUslugeBack = getVrstaUslugeRandomSampleGenerator();

        evidencija.setVrstaUsluge(vrstaUslugeBack);
        assertThat(evidencija.getVrstaUsluge()).isEqualTo(vrstaUslugeBack);

        evidencija.vrstaUsluge(null);
        assertThat(evidencija.getVrstaUsluge()).isNull();
    }

    @Test
    void statusEvidencijeTest() {
        Evidencija evidencija = getEvidencijaRandomSampleGenerator();
        StatusEvidencije statusEvidencijeBack = getStatusEvidencijeRandomSampleGenerator();

        evidencija.setStatusEvidencije(statusEvidencijeBack);
        assertThat(evidencija.getStatusEvidencije()).isEqualTo(statusEvidencijeBack);

        evidencija.statusEvidencije(null);
        assertThat(evidencija.getStatusEvidencije()).isNull();
    }
}
