package org.avd.kamin.service.mapper;

import org.avd.kamin.domain.Grad;
import org.avd.kamin.domain.Naselje;
import org.avd.kamin.service.dto.GradDTO;
import org.avd.kamin.service.dto.NaseljeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Naselje} and its DTO {@link NaseljeDTO}.
 */
@Mapper(componentModel = "spring")
public interface NaseljeMapper extends EntityMapper<NaseljeDTO, Naselje> {
    @Mapping(target = "grad", source = "grad", qualifiedByName = "gradGradNaziv")
    NaseljeDTO toDto(Naselje s);

    @Named("gradGradNaziv")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "gradNaziv", source = "gradNaziv")
    GradDTO toDtoGradGradNaziv(Grad grad);
}
