package org.avd.kamin.service.impl;

import java.util.Optional;
import org.avd.kamin.domain.StatusEvidencije;
import org.avd.kamin.repository.StatusEvidencijeRepository;
import org.avd.kamin.service.StatusEvidencijeService;
import org.avd.kamin.service.dto.StatusEvidencijeDTO;
import org.avd.kamin.service.mapper.StatusEvidencijeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.avd.kamin.domain.StatusEvidencije}.
 */
@Service
@Transactional
public class StatusEvidencijeServiceImpl implements StatusEvidencijeService {

    private static final Logger log = LoggerFactory.getLogger(StatusEvidencijeServiceImpl.class);

    private final StatusEvidencijeRepository statusEvidencijeRepository;

    private final StatusEvidencijeMapper statusEvidencijeMapper;

    public StatusEvidencijeServiceImpl(
        StatusEvidencijeRepository statusEvidencijeRepository,
        StatusEvidencijeMapper statusEvidencijeMapper
    ) {
        this.statusEvidencijeRepository = statusEvidencijeRepository;
        this.statusEvidencijeMapper = statusEvidencijeMapper;
    }

    @Override
    public StatusEvidencijeDTO save(StatusEvidencijeDTO statusEvidencijeDTO) {
        log.debug("Request to save StatusEvidencije : {}", statusEvidencijeDTO);
        StatusEvidencije statusEvidencije = statusEvidencijeMapper.toEntity(statusEvidencijeDTO);
        statusEvidencije = statusEvidencijeRepository.save(statusEvidencije);
        return statusEvidencijeMapper.toDto(statusEvidencije);
    }

    @Override
    public StatusEvidencijeDTO update(StatusEvidencijeDTO statusEvidencijeDTO) {
        log.debug("Request to update StatusEvidencije : {}", statusEvidencijeDTO);
        StatusEvidencije statusEvidencije = statusEvidencijeMapper.toEntity(statusEvidencijeDTO);
        statusEvidencije = statusEvidencijeRepository.save(statusEvidencije);
        return statusEvidencijeMapper.toDto(statusEvidencije);
    }

    @Override
    public Optional<StatusEvidencijeDTO> partialUpdate(StatusEvidencijeDTO statusEvidencijeDTO) {
        log.debug("Request to partially update StatusEvidencije : {}", statusEvidencijeDTO);

        return statusEvidencijeRepository
            .findById(statusEvidencijeDTO.getId())
            .map(existingStatusEvidencije -> {
                statusEvidencijeMapper.partialUpdate(existingStatusEvidencije, statusEvidencijeDTO);

                return existingStatusEvidencije;
            })
            .map(statusEvidencijeRepository::save)
            .map(statusEvidencijeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusEvidencijeDTO> findOne(Long id) {
        log.debug("Request to get StatusEvidencije : {}", id);
        return statusEvidencijeRepository.findById(id).map(statusEvidencijeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StatusEvidencije : {}", id);
        statusEvidencijeRepository.deleteById(id);
    }
}
