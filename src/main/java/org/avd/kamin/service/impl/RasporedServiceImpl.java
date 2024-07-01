package org.avd.kamin.service.impl;

import java.util.Optional;
import org.avd.kamin.domain.Raspored;
import org.avd.kamin.repository.RasporedRepository;
import org.avd.kamin.service.RasporedService;
import org.avd.kamin.service.dto.RasporedDTO;
import org.avd.kamin.service.mapper.RasporedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.avd.kamin.domain.Raspored}.
 */
@Service
@Transactional
public class RasporedServiceImpl implements RasporedService {

    private static final Logger log = LoggerFactory.getLogger(RasporedServiceImpl.class);

    private final RasporedRepository rasporedRepository;

    private final RasporedMapper rasporedMapper;

    public RasporedServiceImpl(RasporedRepository rasporedRepository, RasporedMapper rasporedMapper) {
        this.rasporedRepository = rasporedRepository;
        this.rasporedMapper = rasporedMapper;
    }

    @Override
    public RasporedDTO save(RasporedDTO rasporedDTO) {
        log.debug("Request to save Raspored : {}", rasporedDTO);
        Raspored raspored = rasporedMapper.toEntity(rasporedDTO);
        raspored = rasporedRepository.save(raspored);
        return rasporedMapper.toDto(raspored);
    }

    @Override
    public RasporedDTO update(RasporedDTO rasporedDTO) {
        log.debug("Request to update Raspored : {}", rasporedDTO);
        Raspored raspored = rasporedMapper.toEntity(rasporedDTO);
        raspored = rasporedRepository.save(raspored);
        return rasporedMapper.toDto(raspored);
    }

    @Override
    public Optional<RasporedDTO> partialUpdate(RasporedDTO rasporedDTO) {
        log.debug("Request to partially update Raspored : {}", rasporedDTO);

        return rasporedRepository
            .findById(rasporedDTO.getId())
            .map(existingRaspored -> {
                rasporedMapper.partialUpdate(existingRaspored, rasporedDTO);

                return existingRaspored;
            })
            .map(rasporedRepository::save)
            .map(rasporedMapper::toDto);
    }

    public Page<RasporedDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rasporedRepository.findAllWithEagerRelationships(pageable).map(rasporedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RasporedDTO> findOne(Long id) {
        log.debug("Request to get Raspored : {}", id);
        return rasporedRepository.findOneWithEagerRelationships(id).map(rasporedMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Raspored : {}", id);
        rasporedRepository.deleteById(id);
    }
}
