package org.avd.kamin.service.impl;

import java.util.Optional;
import org.avd.kamin.domain.Evidencija;
import org.avd.kamin.repository.EvidencijaRepository;
import org.avd.kamin.service.EvidencijaService;
import org.avd.kamin.service.dto.EvidencijaDTO;
import org.avd.kamin.service.mapper.EvidencijaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.avd.kamin.domain.Evidencija}.
 */
@Service
@Transactional
public class EvidencijaServiceImpl implements EvidencijaService {

    private static final Logger log = LoggerFactory.getLogger(EvidencijaServiceImpl.class);

    private final EvidencijaRepository evidencijaRepository;

    private final EvidencijaMapper evidencijaMapper;

    public EvidencijaServiceImpl(EvidencijaRepository evidencijaRepository, EvidencijaMapper evidencijaMapper) {
        this.evidencijaRepository = evidencijaRepository;
        this.evidencijaMapper = evidencijaMapper;
    }

    @Override
    public EvidencijaDTO save(EvidencijaDTO evidencijaDTO) {
        log.debug("Request to save Evidencija : {}", evidencijaDTO);
        Evidencija evidencija = evidencijaMapper.toEntity(evidencijaDTO);
        evidencija = evidencijaRepository.save(evidencija);
        return evidencijaMapper.toDto(evidencija);
    }

    @Override
    public EvidencijaDTO update(EvidencijaDTO evidencijaDTO) {
        log.debug("Request to update Evidencija : {}", evidencijaDTO);
        Evidencija evidencija = evidencijaMapper.toEntity(evidencijaDTO);
        evidencija = evidencijaRepository.save(evidencija);
        return evidencijaMapper.toDto(evidencija);
    }

    @Override
    public Optional<EvidencijaDTO> partialUpdate(EvidencijaDTO evidencijaDTO) {
        log.debug("Request to partially update Evidencija : {}", evidencijaDTO);

        return evidencijaRepository
            .findById(evidencijaDTO.getId())
            .map(existingEvidencija -> {
                evidencijaMapper.partialUpdate(existingEvidencija, evidencijaDTO);

                return existingEvidencija;
            })
            .map(evidencijaRepository::save)
            .map(evidencijaMapper::toDto);
    }

    public Page<EvidencijaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return evidencijaRepository.findAllWithEagerRelationships(pageable).map(evidencijaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EvidencijaDTO> findOne(Long id) {
        log.debug("Request to get Evidencija : {}", id);
        return evidencijaRepository.findOneWithEagerRelationships(id).map(evidencijaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evidencija : {}", id);
        evidencijaRepository.deleteById(id);
    }
}
