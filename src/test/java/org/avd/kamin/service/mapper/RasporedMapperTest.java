package org.avd.kamin.service.mapper;

import static org.avd.kamin.domain.RasporedAsserts.*;
import static org.avd.kamin.domain.RasporedTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RasporedMapperTest {

    private RasporedMapper rasporedMapper;

    @BeforeEach
    void setUp() {
        rasporedMapper = new RasporedMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRasporedSample1();
        var actual = rasporedMapper.toEntity(rasporedMapper.toDto(expected));
        assertRasporedAllPropertiesEquals(expected, actual);
    }
}
