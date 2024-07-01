package org.avd.kamin.service.mapper;

import static org.avd.kamin.domain.GradAsserts.*;
import static org.avd.kamin.domain.GradTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GradMapperTest {

    private GradMapper gradMapper;

    @BeforeEach
    void setUp() {
        gradMapper = new GradMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGradSample1();
        var actual = gradMapper.toEntity(gradMapper.toDto(expected));
        assertGradAllPropertiesEquals(expected, actual);
    }
}
