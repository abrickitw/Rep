package org.avd.kamin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NaseljeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NaseljeDTO.class);
        NaseljeDTO naseljeDTO1 = new NaseljeDTO();
        naseljeDTO1.setId(1L);
        NaseljeDTO naseljeDTO2 = new NaseljeDTO();
        assertThat(naseljeDTO1).isNotEqualTo(naseljeDTO2);
        naseljeDTO2.setId(naseljeDTO1.getId());
        assertThat(naseljeDTO1).isEqualTo(naseljeDTO2);
        naseljeDTO2.setId(2L);
        assertThat(naseljeDTO1).isNotEqualTo(naseljeDTO2);
        naseljeDTO1.setId(null);
        assertThat(naseljeDTO1).isNotEqualTo(naseljeDTO2);
    }
}
