package org.avd.kamin.service.mapper;

import org.avd.kamin.domain.Grad;
import org.avd.kamin.service.dto.GradDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Grad} and its DTO {@link GradDTO}.
 */
@Mapper(componentModel = "spring")
public interface GradMapper extends EntityMapper<GradDTO, Grad> {}
