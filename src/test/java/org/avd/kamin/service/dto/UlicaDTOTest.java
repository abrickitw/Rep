package org.avd.kamin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UlicaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UlicaDTO.class);
        UlicaDTO ulicaDTO1 = new UlicaDTO();
        ulicaDTO1.setId(1L);
        UlicaDTO ulicaDTO2 = new UlicaDTO();
        assertThat(ulicaDTO1).isNotEqualTo(ulicaDTO2);
        ulicaDTO2.setId(ulicaDTO1.getId());
        assertThat(ulicaDTO1).isEqualTo(ulicaDTO2);
        ulicaDTO2.setId(2L);
        assertThat(ulicaDTO1).isNotEqualTo(ulicaDTO2);
        ulicaDTO1.setId(null);
        assertThat(ulicaDTO1).isNotEqualTo(ulicaDTO2);
    }
}
