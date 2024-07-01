package org.avd.kamin.service.mapper;

import org.avd.kamin.domain.StatusEvidencije;
import org.avd.kamin.service.dto.StatusEvidencijeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StatusEvidencije} and its DTO {@link StatusEvidencijeDTO}.
 */
@Mapper(componentModel = "spring")
public interface StatusEvidencijeMapper extends EntityMapper<StatusEvidencijeDTO, StatusEvidencije> {}
