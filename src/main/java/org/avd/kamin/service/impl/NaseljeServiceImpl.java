package org.avd.kamin.service.impl;

import java.util.Optional;
import org.avd.kamin.domain.Naselje;
import org.avd.kamin.repository.NaseljeRepository;
import org.avd.kamin.service.NaseljeService;
import org.avd.kamin.service.dto.NaseljeDTO;
import org.avd.kamin.service.mapper.NaseljeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.avd.kamin.domain.Naselje}.
 */
@Service
@Transactional
public class NaseljeServiceImpl implements NaseljeService {

    private static final Logger log = LoggerFactory.getLogger(NaseljeServiceImpl.class);

    private final NaseljeRepository naseljeRepository;

    private final NaseljeMapper naseljeMapper;

    public NaseljeServiceImpl(NaseljeRepository naseljeRepository, NaseljeMapper naseljeMapper) {
        this.naseljeRepository = naseljeRepository;
        this.naseljeMapper = naseljeMapper;
    }

    @Override
    public NaseljeDTO save(NaseljeDTO naseljeDTO) {
        log.debug("Request to save Naselje : {}", naseljeDTO);
        Naselje naselje = naseljeMapper.toEntity(naseljeDTO);
        naselje = naseljeRepository.save(naselje);
        return naseljeMapper.toDto(naselje);
    }

    @Override
    public NaseljeDTO update(NaseljeDTO naseljeDTO) {
        log.debug("Request to update Naselje : {}", naseljeDTO);
        Naselje naselje = naseljeMapper.toEntity(naseljeDTO);
        naselje = naseljeRepository.save(naselje);
        return naseljeMapper.toDto(naselje);
    }

    @Override
    public Optional<NaseljeDTO> partialUpdate(NaseljeDTO naseljeDTO) {
        log.debug("Request to partially update Naselje : {}", naseljeDTO);

        return naseljeRepository
            .findById(naseljeDTO.getId())
            .map(existingNaselje -> {
                naseljeMapper.partialUpdate(existingNaselje, naseljeDTO);

                return existingNaselje;
            })
            .map(naseljeRepository::save)
            .map(naseljeMapper::toDto);
    }

    public Page<NaseljeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return naseljeRepository.findAllWithEagerRelationships(pageable).map(naseljeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NaseljeDTO> findOne(Long id) {
        log.debug("Request to get Naselje : {}", id);
        return naseljeRepository.findOneWithEagerRelationships(id).map(naseljeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Naselje : {}", id);
        naseljeRepository.deleteById(id);
    }
}
