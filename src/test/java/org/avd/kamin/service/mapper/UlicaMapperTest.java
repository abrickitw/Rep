package org.avd.kamin.service.mapper;

import static org.avd.kamin.domain.UlicaAsserts.*;
import static org.avd.kamin.domain.UlicaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UlicaMapperTest {

    private UlicaMapper ulicaMapper;

    @BeforeEach
    void setUp() {
        ulicaMapper = new UlicaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUlicaSample1();
        var actual = ulicaMapper.toEntity(ulicaMapper.toDto(expected));
        assertUlicaAllPropertiesEquals(expected, actual);
    }
}
