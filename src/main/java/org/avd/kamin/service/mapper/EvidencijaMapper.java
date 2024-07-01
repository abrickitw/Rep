package org.avd.kamin.service.mapper;

import org.avd.kamin.domain.Evidencija;
import org.avd.kamin.domain.Raspored;
import org.avd.kamin.domain.StatusEvidencije;
import org.avd.kamin.domain.User;
import org.avd.kamin.domain.VrstaUsluge;
import org.avd.kamin.service.dto.EvidencijaDTO;
import org.avd.kamin.service.dto.RasporedDTO;
import org.avd.kamin.service.dto.StatusEvidencijeDTO;
import org.avd.kamin.service.dto.UserDTO;
import org.avd.kamin.service.dto.VrstaUslugeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evidencija} and its DTO {@link EvidencijaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EvidencijaMapper extends EntityMapper<EvidencijaDTO, Evidencija> {
    @Mapping(target = "korisnikIzvrsio", source = "korisnikIzvrsio", qualifiedByName = "userLogin")
    @Mapping(target = "korisnikIspravio", source = "korisnikIspravio", qualifiedByName = "userLogin")
    @Mapping(target = "raspored", source = "raspored", qualifiedByName = "rasporedDatumUsluge")
    @Mapping(target = "vrstaUsluge", source = "vrstaUsluge", qualifiedByName = "vrstaUslugeVrstaUslugeNaziv")
    @Mapping(target = "statusEvidencije", source = "statusEvidencije", qualifiedByName = "statusEvidencijeStatusEvidencijeNaziv")
    EvidencijaDTO toDto(Evidencija s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("rasporedDatumUsluge")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "datumUsluge", source = "datumUsluge")
    RasporedDTO toDtoRasporedDatumUsluge(Raspored raspored);

    @Named("vrstaUslugeVrstaUslugeNaziv")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "vrstaUslugeNaziv", source = "vrstaUslugeNaziv")
    VrstaUslugeDTO toDtoVrstaUslugeVrstaUslugeNaziv(VrstaUsluge vrstaUsluge);

    @Named("statusEvidencijeStatusEvidencijeNaziv")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statusEvidencijeNaziv", source = "statusEvidencijeNaziv")
    StatusEvidencijeDTO toDtoStatusEvidencijeStatusEvidencijeNaziv(StatusEvidencije statusEvidencije);
}
