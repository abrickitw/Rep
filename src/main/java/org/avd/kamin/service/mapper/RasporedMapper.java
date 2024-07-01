package org.avd.kamin.service.mapper;

import org.avd.kamin.domain.Grad;
import org.avd.kamin.domain.Naselje;
import org.avd.kamin.domain.Raspored;
import org.avd.kamin.domain.Ulica;
import org.avd.kamin.domain.User;
import org.avd.kamin.service.dto.GradDTO;
import org.avd.kamin.service.dto.NaseljeDTO;
import org.avd.kamin.service.dto.RasporedDTO;
import org.avd.kamin.service.dto.UlicaDTO;
import org.avd.kamin.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Raspored} and its DTO {@link RasporedDTO}.
 */
@Mapper(componentModel = "spring")
public interface RasporedMapper extends EntityMapper<RasporedDTO, Raspored> {
    @Mapping(target = "grad", source = "grad", qualifiedByName = "gradGradNaziv")
    @Mapping(target = "naselje", source = "naselje", qualifiedByName = "naseljeNaseljeNaziv")
    @Mapping(target = "ulica", source = "ulica", qualifiedByName = "ulicaUlicaNaziv")
    @Mapping(target = "korisnikKreirao", source = "korisnikKreirao", qualifiedByName = "userLogin")
    RasporedDTO toDto(Raspored s);

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

    @Named("ulicaUlicaNaziv")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ulicaNaziv", source = "ulicaNaziv")
    UlicaDTO toDtoUlicaUlicaNaziv(Ulica ulica);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
