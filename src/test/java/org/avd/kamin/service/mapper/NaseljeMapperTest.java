package org.avd.kamin.service.mapper;

import static org.avd.kamin.domain.NaseljeAsserts.*;
import static org.avd.kamin.domain.NaseljeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NaseljeMapperTest {

    private NaseljeMapper naseljeMapper;

    @BeforeEach
    void setUp() {
        naseljeMapper = new NaseljeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNaseljeSample1();
        var actual = naseljeMapper.toEntity(naseljeMapper.toDto(expected));
        assertNaseljeAllPropertiesEquals(expected, actual);
    }
}
