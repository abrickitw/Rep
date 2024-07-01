package org.avd.kamin.service.impl;

import java.util.Optional;
import org.avd.kamin.domain.Grad;
import org.avd.kamin.repository.GradRepository;
import org.avd.kamin.service.GradService;
import org.avd.kamin.service.dto.GradDTO;
import org.avd.kamin.service.mapper.GradMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.avd.kamin.domain.Grad}.
 */
@Service
@Transactional
public class GradServiceImpl implements GradService {

    private static final Logger log = LoggerFactory.getLogger(GradServiceImpl.class);

    private final GradRepository gradRepository;

    private final GradMapper gradMapper;

    public GradServiceImpl(GradRepository gradRepository, GradMapper gradMapper) {
        this.gradRepository = gradRepository;
        this.gradMapper = gradMapper;
    }

    @Override
    public GradDTO save(GradDTO gradDTO) {
        log.debug("Request to save Grad : {}", gradDTO);
        Grad grad = gradMapper.toEntity(gradDTO);
        grad = gradRepository.save(grad);
        return gradMapper.toDto(grad);
    }

    @Override
    public GradDTO update(GradDTO gradDTO) {
        log.debug("Request to update Grad : {}", gradDTO);
        Grad grad = gradMapper.toEntity(gradDTO);
        grad = gradRepository.save(grad);
        return gradMapper.toDto(grad);
    }

    @Override
    public Optional<GradDTO> partialUpdate(GradDTO gradDTO) {
        log.debug("Request to partially update Grad : {}", gradDTO);

        return gradRepository
            .findById(gradDTO.getId())
            .map(existingGrad -> {
                gradMapper.partialUpdate(existingGrad, gradDTO);

                return existingGrad;
            })
            .map(gradRepository::save)
            .map(gradMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GradDTO> findOne(Long id) {
        log.debug("Request to get Grad : {}", id);
        return gradRepository.findById(id).map(gradMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Grad : {}", id);
        gradRepository.deleteById(id);
    }
}
