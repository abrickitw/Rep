package org.avd.kamin.service.mapper;

import org.avd.kamin.domain.Grad;
import org.avd.kamin.domain.Naselje;
import org.avd.kamin.domain.Ulica;
import org.avd.kamin.service.dto.GradDTO;
import org.avd.kamin.service.dto.NaseljeDTO;
import org.avd.kamin.service.dto.UlicaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ulica} and its DTO {@link UlicaDTO}.
 */
@Mapper(componentModel = "spring")
public interface UlicaMapper extends EntityMapper<UlicaDTO, Ulica> {
    @Mapping(target = "grad", source = "grad", qualifiedByName = "gradGradNaziv")
    @Mapping(target = "naselje", source = "naselje", qualifiedByName = "naseljeNaseljeNaziv")
    UlicaDTO toDto(Ulica s);

    @Named("gradGradNaziv")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "gradNaziv", source = "gradNaziv")
    GradDTO toDtoGradGradNaziv(Grad grad);

    @Named("naseljeNaseljeNaziv")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "naseljeNaziv", source = "naseljeNaziv")
    NaseljeDTO toDtoNaseljeNaseljeNaziv(Naselje naselje);
}
