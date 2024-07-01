package org.avd.kamin.service.mapper;

import static org.avd.kamin.domain.StatusEvidencijeAsserts.*;
import static org.avd.kamin.domain.StatusEvidencijeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusEvidencijeMapperTest {

    private StatusEvidencijeMapper statusEvidencijeMapper;

    @BeforeEach
    void setUp() {
        statusEvidencijeMapper = new StatusEvidencijeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStatusEvidencijeSample1();
        var actual = statusEvidencijeMapper.toEntity(statusEvidencijeMapper.toDto(expected));
        assertStatusEvidencijeAllPropertiesEquals(expected, actual);
    }
}
