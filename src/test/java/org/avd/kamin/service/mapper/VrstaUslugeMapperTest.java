package org.avd.kamin.service.mapper;

import static org.avd.kamin.domain.VrstaUslugeAsserts.*;
import static org.avd.kamin.domain.VrstaUslugeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VrstaUslugeMapperTest {

    private VrstaUslugeMapper vrstaUslugeMapper;

    @BeforeEach
    void setUp() {
        vrstaUslugeMapper = new VrstaUslugeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVrstaUslugeSample1();
        var actual = vrstaUslugeMapper.toEntity(vrstaUslugeMapper.toDto(expected));
        assertVrstaUslugeAllPropertiesEquals(expected, actual);
    }
}
