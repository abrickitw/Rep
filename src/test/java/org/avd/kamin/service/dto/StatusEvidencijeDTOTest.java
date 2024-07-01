package org.avd.kamin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusEvidencijeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusEvidencijeDTO.class);
        StatusEvidencijeDTO statusEvidencijeDTO1 = new StatusEvidencijeDTO();
        statusEvidencijeDTO1.setId(1L);
        StatusEvidencijeDTO statusEvidencijeDTO2 = new StatusEvidencijeDTO();
        assertThat(statusEvidencijeDTO1).isNotEqualTo(statusEvidencijeDTO2);
        statusEvidencijeDTO2.setId(statusEvidencijeDTO1.getId());
        assertThat(statusEvidencijeDTO1).isEqualTo(statusEvidencijeDTO2);
        statusEvidencijeDTO2.setId(2L);
        assertThat(statusEvidencijeDTO1).isNotEqualTo(statusEvidencijeDTO2);
        statusEvidencijeDTO1.setId(null);
        assertThat(statusEvidencijeDTO1).isNotEqualTo(statusEvidencijeDTO2);
    }
}
