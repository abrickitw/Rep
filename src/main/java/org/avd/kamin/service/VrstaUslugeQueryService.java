package org.avd.kamin.service;

import org.avd.kamin.domain.*; // for static metamodels
import org.avd.kamin.domain.VrstaUsluge;
import org.avd.kamin.repository.VrstaUslugeRepository;
import org.avd.kamin.service.criteria.VrstaUslugeCriteria;
import org.avd.kamin.service.dto.VrstaUslugeDTO;
import org.avd.kamin.service.mapper.VrstaUslugeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link VrstaUsluge} entities in the database.
 * The main input is a {@link VrstaUslugeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link VrstaUslugeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VrstaUslugeQueryService extends QueryService<VrstaUsluge> {

    private static final Logger log = LoggerFactory.getLogger(VrstaUslugeQueryService.class);

    private final VrstaUslugeRepository vrstaUslugeRepository;

    private final VrstaUslugeMapper vrstaUslugeMapper;

    public VrstaUslugeQueryService(VrstaUslugeRepository vrstaUslugeRepository, VrstaUslugeMapper vrstaUslugeMapper) {
        this.vrstaUslugeRepository = vrstaUslugeRepository;
        this.vrstaUslugeMapper = vrstaUslugeMapper;
    }

    /**
     * Return a {@link Page} of {@link VrstaUslugeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VrstaUslugeDTO> findByCriteria(VrstaUslugeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VrstaUsluge> specification = createSpecification(criteria);
        return vrstaUslugeRepository.findAll(specification, page).map(vrstaUslugeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VrstaUslugeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VrstaUsluge> specification = createSpecification(criteria);
        return vrstaUslugeRepository.count(specification);
    }

    /**
     * Function to convert {@link VrstaUslugeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VrstaUsluge> createSpecification(VrstaUslugeCriteria criteria) {
        Specification<VrstaUsluge> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VrstaUsluge_.id));
            }
            if (criteria.getVrstaUslugeNaziv() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVrstaUslugeNaziv(), VrstaUsluge_.vrstaUslugeNaziv));
            }
        }
        return specification;
    }
}
