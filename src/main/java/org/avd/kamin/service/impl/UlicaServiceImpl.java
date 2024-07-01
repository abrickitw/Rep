package org.avd.kamin.service.impl;

import java.util.Optional;
import org.avd.kamin.domain.Ulica;
import org.avd.kamin.repository.UlicaRepository;
import org.avd.kamin.service.UlicaService;
import org.avd.kamin.service.dto.UlicaDTO;
import org.avd.kamin.service.mapper.UlicaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.avd.kamin.domain.Ulica}.
 */
@Service
@Transactional
public class UlicaServiceImpl implements UlicaService {

    private static final Logger log = LoggerFactory.getLogger(UlicaServiceImpl.class);

    private final UlicaRepository ulicaRepository;

    private final UlicaMapper ulicaMapper;

    public UlicaServiceImpl(UlicaRepository ulicaRepository, UlicaMapper ulicaMapper) {
        this.ulicaRepository = ulicaRepository;
        this.ulicaMapper = ulicaMapper;
    }

    @Override
    public UlicaDTO save(UlicaDTO ulicaDTO) {
        log.debug("Request to save Ulica : {}", ulicaDTO);
        Ulica ulica = ulicaMapper.toEntity(ulicaDTO);
        ulica = ulicaRepository.save(ulica);
        return ulicaMapper.toDto(ulica);
    }

    @Override
    public UlicaDTO update(UlicaDTO ulicaDTO) {
        log.debug("Request to update Ulica : {}", ulicaDTO);
        Ulica ulica = ulicaMapper.toEntity(ulicaDTO);
        ulica = ulicaRepository.save(ulica);
        return ulicaMapper.toDto(ulica);
    }

    @Override
    public Optional<UlicaDTO> partialUpdate(UlicaDTO ulicaDTO) {
        log.debug("Request to partially update Ulica : {}", ulicaDTO);

        return ulicaRepository
            .findById(ulicaDTO.getId())
            .map(existingUlica -> {
                ulicaMapper.partialUpdate(existingUlica, ulicaDTO);

                return existingUlica;
            })
            .map(ulicaRepository::save)
            .map(ulicaMapper::toDto);
    }

    public Page<UlicaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ulicaRepository.findAllWithEagerRelationships(pageable).map(ulicaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UlicaDTO> findOne(Long id) {
        log.debug("Request to get Ulica : {}", id);
        return ulicaRepository.findOneWithEagerRelationships(id).map(ulicaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ulica : {}", id);
        ulicaRepository.deleteById(id);
    }
}
