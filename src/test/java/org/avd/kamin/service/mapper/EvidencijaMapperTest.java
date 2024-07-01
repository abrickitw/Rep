package org.avd.kamin.service.mapper;

import static org.avd.kamin.domain.EvidencijaAsserts.*;
import static org.avd.kamin.domain.EvidencijaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvidencijaMapperTest {

    private EvidencijaMapper evidencijaMapper;

    @BeforeEach
    void setUp() {
        evidencijaMapper = new EvidencijaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEvidencijaSample1();
        var actual = evidencijaMapper.toEntity(evidencijaMapper.toDto(expected));
        assertEvidencijaAllPropertiesEquals(expected, actual);
    }
}
