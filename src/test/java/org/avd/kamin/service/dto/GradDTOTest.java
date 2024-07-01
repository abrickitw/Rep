package org.avd.kamin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GradDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GradDTO.class);
        GradDTO gradDTO1 = new GradDTO();
        gradDTO1.setId(1L);
        GradDTO gradDTO2 = new GradDTO();
        assertThat(gradDTO1).isNotEqualTo(gradDTO2);
        gradDTO2.setId(gradDTO1.getId());
        assertThat(gradDTO1).isEqualTo(gradDTO2);
        gradDTO2.setId(2L);
        assertThat(gradDTO1).isNotEqualTo(gradDTO2);
        gradDTO1.setId(null);
        assertThat(gradDTO1).isNotEqualTo(gradDTO2);
    }
}
