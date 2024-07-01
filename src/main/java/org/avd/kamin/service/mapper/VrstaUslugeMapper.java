package org.avd.kamin.service.mapper;

import org.avd.kamin.domain.VrstaUsluge;
import org.avd.kamin.service.dto.VrstaUslugeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VrstaUsluge} and its DTO {@link VrstaUslugeDTO}.
 */
@Mapper(componentModel = "spring")
public interface VrstaUslugeMapper extends EntityMapper<VrstaUslugeDTO, VrstaUsluge> {}
