package org.avd.kamin.service.impl;

import java.util.Optional;
import org.avd.kamin.domain.VrstaUsluge;
import org.avd.kamin.repository.VrstaUslugeRepository;
import org.avd.kamin.service.VrstaUslugeService;
import org.avd.kamin.service.dto.VrstaUslugeDTO;
import org.avd.kamin.service.mapper.VrstaUslugeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.avd.kamin.domain.VrstaUsluge}.
 */
@Service
@Transactional
public class VrstaUslugeServiceImpl implements VrstaUslugeService {

    private static final Logger log = LoggerFactory.getLogger(VrstaUslugeServiceImpl.class);

    private final VrstaUslugeRepository vrstaUslugeRepository;

    private final VrstaUslugeMapper vrstaUslugeMapper;

    public VrstaUslugeServiceImpl(VrstaUslugeRepository vrstaUslugeRepository, VrstaUslugeMapper vrstaUslugeMapper) {
        this.vrstaUslugeRepository = vrstaUslugeRepository;
        this.vrstaUslugeMapper = vrstaUslugeMapper;
    }

    @Override
    public VrstaUslugeDTO save(VrstaUslugeDTO vrstaUslugeDTO) {
        log.debug("Request to save VrstaUsluge : {}", vrstaUslugeDTO);
        VrstaUsluge vrstaUsluge = vrstaUslugeMapper.toEntity(vrstaUslugeDTO);
        vrstaUsluge = vrstaUslugeRepository.save(vrstaUsluge);
        return vrstaUslugeMapper.toDto(vrstaUsluge);
    }

    @Override
    public VrstaUslugeDTO update(VrstaUslugeDTO vrstaUslugeDTO) {
        log.debug("Request to update VrstaUsluge : {}", vrstaUslugeDTO);
        VrstaUsluge vrstaUsluge = vrstaUslugeMapper.toEntity(vrstaUslugeDTO);
        vrstaUsluge = vrstaUslugeRepository.save(vrstaUsluge);
        return vrstaUslugeMapper.toDto(vrstaUsluge);
    }

    @Override
    public Optional<VrstaUslugeDTO> partialUpdate(VrstaUslugeDTO vrstaUslugeDTO) {
        log.debug("Request to partially update VrstaUsluge : {}", vrstaUslugeDTO);

        return vrstaUslugeRepository
            .findById(vrstaUslugeDTO.getId())
            .map(existingVrstaUsluge -> {
                vrstaUslugeMapper.partialUpdate(existingVrstaUsluge, vrstaUslugeDTO);

                return existingVrstaUsluge;
            })
            .map(vrstaUslugeRepository::save)
            .map(vrstaUslugeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VrstaUslugeDTO> findOne(Long id) {
        log.debug("Request to get VrstaUsluge : {}", id);
        return vrstaUslugeRepository.findById(id).map(vrstaUslugeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VrstaUsluge : {}", id);
        vrstaUslugeRepository.deleteById(id);
    }
}
