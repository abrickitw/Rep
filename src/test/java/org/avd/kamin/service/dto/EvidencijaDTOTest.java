package org.avd.kamin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.avd.kamin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvidencijaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvidencijaDTO.class);
        EvidencijaDTO evidencijaDTO1 = new EvidencijaDTO();
        evidencijaDTO1.setId(1L);
        EvidencijaDTO evidencijaDTO2 = new EvidencijaDTO();
        assertThat(evidencijaDTO1).isNotEqualTo(evidencijaDTO2);
        evidencijaDTO2.setId(evidencijaDTO1.getId());
        assertThat(evidencijaDTO1).isEqualTo(evidencijaDTO2);
        evidencijaDTO2.setId(2L);
        assertThat(evidencijaDTO1).isNotEqualTo(evidencijaDTO2);
        evidencijaDTO1.setId(null);
        assertThat(evidencijaDTO1).isNotEqualTo(evidencijaDTO2);
    }
}
