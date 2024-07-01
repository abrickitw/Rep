package org.avd.kamin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RasporedDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RasporedDTO.class);
        RasporedDTO rasporedDTO1 = new RasporedDTO();
        rasporedDTO1.setId(1L);
        RasporedDTO rasporedDTO2 = new RasporedDTO();
        assertThat(rasporedDTO1).isNotEqualTo(rasporedDTO2);
        rasporedDTO2.setId(rasporedDTO1.getId());
        assertThat(rasporedDTO1).isEqualTo(rasporedDTO2);
        rasporedDTO2.setId(2L);
        assertThat(rasporedDTO1).isNotEqualTo(rasporedDTO2);
        rasporedDTO1.setId(null);
        assertThat(rasporedDTO1).isNotEqualTo(rasporedDTO2);
    }
}
