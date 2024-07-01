package org.avd.kamin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VrstaUslugeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VrstaUslugeDTO.class);
        VrstaUslugeDTO vrstaUslugeDTO1 = new VrstaUslugeDTO();
        vrstaUslugeDTO1.setId(1L);
        VrstaUslugeDTO vrstaUslugeDTO2 = new VrstaUslugeDTO();
        assertThat(vrstaUslugeDTO1).isNotEqualTo(vrstaUslugeDTO2);
        vrstaUslugeDTO2.setId(vrstaUslugeDTO1.getId());
        assertThat(vrstaUslugeDTO1).isEqualTo(vrstaUslugeDTO2);
        vrstaUslugeDTO2.setId(2L);
        assertThat(vrstaUslugeDTO1).isNotEqualTo(vrstaUslugeDTO2);
        vrstaUslugeDTO1.setId(null);
        assertThat(vrstaUslugeDTO1).isNotEqualTo(vrstaUslugeDTO2);
    }
}
